plugins {
    val kotlinVersion = "1.8.0"
    val gradleVersion = "8.0.0-alpha11"

    id("com.android.application").version(gradleVersion).apply(false)
    id("com.android.library").version(gradleVersion).apply(false)
    kotlin("android").version(kotlinVersion).apply(false)
}

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
        classpath("com.android.tools.build:gradle:8.0.0-alpha11")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
