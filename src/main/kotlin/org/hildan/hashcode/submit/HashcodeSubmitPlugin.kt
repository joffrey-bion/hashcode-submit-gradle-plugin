package org.hildan.hashcode.submit

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.bundling.Zip
import org.gradle.process.CommandLineArgumentProvider
import java.nio.file.Path
import java.nio.file.Paths
import javax.inject.Inject

private const val EXTENSION_NAME = "hashcode"
private const val ZIP_TASK_NAME = "srcZip"
private const val RUN_TASK_NAME = "runAllInputs"
private const val SUBMIT_TASK_NAME = "submit"

@Suppress("unused")
open class HashcodeSubmitPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val ext = project.extensions.create(EXTENSION_NAME, HashcodeExtension::class.java, project)

        val srcZipTask = project.tasks.create(ZIP_TASK_NAME, ZipSourcesTask::class.java, ext)
        val runTask = project.tasks.create(RUN_TASK_NAME, RunTask::class.java, ext)
        val submitTask = project.tasks.create(SUBMIT_TASK_NAME, SubmitTask::class.java, ext)
        submitTask.dependsOn(srcZipTask)
        submitTask.dependsOn(runTask)
    }
}

open class HashcodeExtension(private val project: Project) {
    var inputsDir: Path = Paths.get("${project.projectDir}/inputs")
    var outputsDir: Path = Paths.get("${project.projectDir}/outputs")
    var srcZipOutputFile: Path = outputsDir.resolve("sources.zip")
}

open class ZipSourcesTask @Inject constructor(private val ext: HashcodeExtension) : Zip() {

    init {
        group = "hashcode"
        description = "Zips all sources to submit them along with output files."

        project.afterEvaluate {
            archiveFileName.set(ext.srcZipOutputFile.toString())
            from(project.getMainSourceSet().allSource)
        }
    }
}

open class RunTask @Inject constructor(private val ext: HashcodeExtension) : JavaExec() {

    init {
        group = "hashcode"
        description = "Runs the program on all input problems."

        argumentProviders.add(CommandLineArgumentProvider {
            // we pass all files that are in the inputs folder
            project.fileTree(ext.inputsDir).files.map { it.path }
        })

        project.afterEvaluate {
            classpath = project.getMainSourceSet().runtimeClasspath
            main = project.getMainClassName()
        }
    }
}

open class SubmitTask @Inject constructor(private val ext: HashcodeExtension) : DefaultTask() {
    init {
        group = "hashcode"
        description = "Prepares the submission by running all necessary tasks."
    }
}

private fun Project.getMainSourceSet(): SourceSet = javaPlugin.sourceSets.getByName("main")

@Suppress("UnstableApiUsage")
private fun Project.getMainClassName(): String = project.applicationExtension.let {
    it.mainClass.orNull ?: it.mainClassName
}

private val Project.javaPlugin
    get() = convention.findPlugin(JavaPluginConvention::class.java)
        ?: throw GradleException("Kotlin or Java plugin must be applied to the project to find sources to Zip")

private val Project.applicationExtension
    get() = extensions.findByType(JavaApplication::class.java)
        ?: throw GradleException("the Application plugin must be applied to the project to provide the main class")
