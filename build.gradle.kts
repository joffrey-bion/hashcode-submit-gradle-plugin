plugins {
    kotlin("jvm") version "1.4.20" // aligned with Gradle 6.8.1
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.12.0"
    id("org.hildan.github.changelog") version "1.3.0"
}

group = "org.hildan.hashcode"

repositories {
    jcenter()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
}

pluginBundle {
    website = "https://github.com/joffrey-bion/hashcode-gradle-plugin"
    vcsUrl = "https://github.com/joffrey-bion/hashcode-gradle-plugin"
    tags = listOf("google", "hashcode", "submit", "zip")
}

gradlePlugin {
    plugins {
        create("hashcodeSubmitPlugin") {
            id = "org.hildan.hashcode-submit"
            displayName = "Hashcode Submit Plugin"
            description = "Provides tasks to help submit answers to Google Hashcode"
            implementationClass = "org.hildan.hashcode.submit.HashcodeSubmitPlugin"
        }
    }
}

changelog {
    futureVersionTag = project.version.toString()
}
