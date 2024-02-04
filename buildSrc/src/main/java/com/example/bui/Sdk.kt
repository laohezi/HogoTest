package com.example.bui

import org.gradle.api.JavaVersion

object Sdk {
    val compileSdkVersion = 34
    val targetSdkVersion = 33
    val minSdkVersion = 26
    val buildToolsVersion = "34.0.0"
}

object Jdk{
    val sourceCompatibility = JavaVersion.VERSION_17
    val targetCompatibility = JavaVersion.VERSION_17
    val kotlinJvmTarget = "17"
}

object Ksp{
    val version = "1.9.21-1.0.15"
    val plugins = "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:$version"
}