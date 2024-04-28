import com.example.bui.AndroidX
import com.example.bui.Hilt
import com.example.bui.Jdk
import com.example.bui.Sdk

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    //add ksp plugin
}

android {
    namespace = "com.hugo.mylibrary"
    compileSdk = Sdk.compileSdkVersion

    defaultConfig {
        minSdk = Sdk.minSdkVersion
        targetSdk = Sdk.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Jdk.sourceCompatibility
        targetCompatibility = Jdk.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = Jdk.kotlinJvmTarget
    }
}

dependencies {

    api(com.example.bui.UtilsCode)
    api(AndroidX.appcompat)
    implementation(Hilt.hilt)
    kapt(Hilt.compiler)

}