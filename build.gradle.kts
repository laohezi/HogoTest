import com.example.bui.Kotlin

// Top-level build file where you can add configuration options common to all sub-projects/modules.
val kotlin_version = Kotlin.version

buildscript {

    repositories {
        google()
        jcenter()

    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.1")
        //classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath(kotlin("gradle-plugin", version = "1.9.21"))
        //ksp
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.21-1.0.15")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    kotlin("jvm") version "1.9.21" apply false



}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}