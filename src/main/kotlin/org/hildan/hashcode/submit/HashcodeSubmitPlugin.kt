package org.hildan.hashcode.submit

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.*
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.bundling.Zip
import org.gradle.kotlin.dsl.*
import java.nio.file.Path
import java.nio.file.Paths

private const val HASHCODE_EXTENSION_NAME = "hashcode"
private const val HASHCODE_TASK_GROUP = "hashcode"

private const val ZIP_TASK_NAME = "srcZip"
private const val RUN_TASK_NAME = "runAllInputs"
private const val SUBMIT_TASK_NAME = "submit"

@Suppress("unused")
open class HashcodeSubmitPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val ext = project.extensions.create<HashcodeExtension>(HASHCODE_EXTENSION_NAME, project)

        val srcZipTask = registerZipTask(project, ext)
        val runTask = registerRunTask(project, ext)

        project.tasks.register(SUBMIT_TASK_NAME) {
            group = HASHCODE_TASK_GROUP
            description = "Prepares the submission by running all necessary tasks."

            dependsOn(srcZipTask)
            dependsOn(runTask)
        }
    }
}

open class HashcodeExtension(val project: Project) {
    /**
     * The path to directory containing input files.
     */
    var inputsDir: Path = Paths.get("${project.projectDir}/inputs")

    /**
     * The path to the output ZIP file to create when zipping the sources.
     */
    var srcZipFile: Path = Paths.get("${project.projectDir}/outputs/sources.zip")
}

private fun registerZipTask(project: Project, ext: HashcodeExtension) =
    project.tasks.register<Zip>(ZIP_TASK_NAME) {
        group = HASHCODE_TASK_GROUP
        description = "Zips all sources to submit them along with output files."

        archiveFileName.set(ext.srcZipFile.toString())
        from(project.getMainSourceSet().allSource)
    }

private fun registerRunTask(project: Project, ext: HashcodeExtension) =
    project.tasks.register<JavaExec>(RUN_TASK_NAME) {
        group = HASHCODE_TASK_GROUP
        description = "Runs the program on all input problems."
        classpath = project.getMainSourceSet().runtimeClasspath
        mainClass.set(project.applicationExtension.mainClass)

        argumentProviders.add {
            // we pass all files that are in the inputs folder
            project.fileTree(ext.inputsDir).files.map { it.path }
        }
    }

private fun Project.getMainSourceSet(): SourceSet = javaPlugin.sourceSets.getByName("main")

private val Project.javaPlugin
    get() = convention.findPlugin(JavaPluginConvention::class.java)
        ?: throw GradleException("Kotlin or Java plugin must be applied to the project to find sources to Zip")

private val Project.applicationExtension
    get() = extensions.findByType<JavaApplication>()
        ?: throw GradleException("the Application plugin must be applied to the project to provide the main class")
