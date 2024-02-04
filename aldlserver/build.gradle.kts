import com.example.bui.AndroidX
import com.example.bui.Jdk
import com.example.bui.Material
import com.example.bui.Sdk
import com.example.bui.Test

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = Sdk.compileSdkVersion

    defaultConfig {
        applicationId = "com.example.com.example.aldlserver"
        minSdk = Sdk.minSdkVersion
        targetSdk = Sdk.targetSdkVersion
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    namespace = "com.example.aldlserver"
}

dependencies {

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appcompat)
    implementation(Material.material)
    testImplementation(Test.junit)

}