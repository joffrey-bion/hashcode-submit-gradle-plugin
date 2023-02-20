plugins {
    `kotlin-dsl` // compiler plugin to deal with Gradle's Action<T> and the likes
    id("com.gradle.plugin-publish") version "1.1.0"
    id("org.hildan.github.changelog") version "1.13.0"
}

group = "org.hildan.hashcode"

repositories {
    mavenCentral()
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
