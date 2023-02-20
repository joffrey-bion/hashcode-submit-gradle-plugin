plugins {
    kotlin("jvm") version "1.4.20" // aligned with Gradle 6.8.1
    `kotlin-dsl` // compiler plugin to deal with Gradle's Action<T> and the likes
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "1.1.0"
    id("org.hildan.github.changelog") version "1.3.0"
}

group = "org.hildan.hashcode"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
}

gradlePlugin {
    plugins {
        create("hashcodeSubmitPlugin") {
            id = "org.hildan.hashcode-submit"
            displayName = "Hashcode Submit Plugin"
            description = "Provides tasks to help submit answers to Google Hashcode"
            implementationClass = "org.hildan.hashcode.submit.HashcodeSubmitPlugin"
            website.set("https://github.com/joffrey-bion/hashcode-gradle-plugin")
            vcsUrl.set("https://github.com/joffrey-bion/hashcode-gradle-plugin")
            tags.set(listOf("google", "hashcode", "submit", "zip"))
        }
    }
}

changelog {
    futureVersionTag = project.version.toString()
}
