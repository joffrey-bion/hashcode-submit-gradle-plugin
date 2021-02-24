# HashCode Submit Gradle Plugin

[![Gradle plugin version](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/org/hildan/hashcode-submit/org.hildan.hashcode-submit.gradle.plugin/maven-metadata.xml.svg?label=gradle&logo=gradle)](https://plugins.gradle.org/plugin/org.hildan.hashcode-submit)
[![Github Build](https://img.shields.io/github/workflow/status/joffrey-bion/hashcode-submit-gradle-plugin/CI%20Build?label=build&logo=github)](https://github.com/joffrey-bion/hashcode-submit-gradle-plugin/actions?query=workflow%3A%22CI+Build%22)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/joffrey-bion/hashcode-submit-gradle-plugin/blob/master/LICENSE)

A Gradle plugin to help submit solutions to the Google HashCode competition

This plugin adds 3 tasks:

* `srcZip`: creates a ZIP file for all sources from the `main` source set
* `runAllInputs`: runs your main class, passing all input files from the input folder as arguments
* `submit`: an empty task that just depends on both `srcZip` and `runAllInputs`

The locations of the input files and output ZIP file are configurable (see below).

### Prerequisites

* the Java or Kotlin plugin, to determine the `main` source set
* the `application` plugin (to define your main class)

### Usage

Apply the plugin this way:

```kotlin
plugins {
    application
    id("org.hildan.hashcode-submit") version "0.2.0"
}

application {
    mainClass.set("com.example.MainKt")
}
```

Additional configuration and its default values:

```kotlin
hashcode {
    // directory where all input files reside
    inputsDir = Paths.get("${project.projectDir}/inputs")
    // the path to the ZIP file of the sources
    srcZipFile = Paths.get("${project.projectDir}/outputs/sources.zip")
}
```