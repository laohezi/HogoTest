import com.example.bui.*
plugins {
    id("com.android.application")
    id("kotlin-android")
}




android {
    buildToolsVersion= Sdk.buildToolsVersion
    compileSdk =Sdk.compileSdkVersion
    defaultConfig {
        applicationId = "com.example.jetnews1"
        minSdk= Sdk.minSdkVersion
        targetSdk =Sdk.targetSdkVersion
        versionCode =1
        versionName ="1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner= "androidx.test.runner.AndroidJUnitRunner"
    }

   /* signingConfigs {
        // We use a bundled debug keystore, to allow debug builds from CI to be upgradable
        debug {
            storeFile = rootProject.file("debug.keystore")
            storePassword "android"
            keyAlias "androiddebugkey"
            keyPassword "android"
        }
    }*/

    buildTypes {

        debug {
            signingConfig = signingConfigs.getByName("debug")

        }

        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility =Jdk.sourceCompatibility
        targetCompatibility = Jdk.targetCompatibility
    }

    kotlinOptions {
        jvmTarget = Jdk.kotlinJvmTarget
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.version
    }
    packagingOptions {
        resources {
            excludes += setOf("/META-INF/AL2.0", "/META-INF/LGPL2.1")
        }
    }

    namespace = "com.example.app1"
}

dependencies {
    implementation(Coroutines.android)
    implementation(Compose.ui)
    implementation (Compose.foundation)
    implementation (Compose.material)
    implementation (Compose.materialIconsExtended)
    implementation (Compose.tooling)

    implementation(Compose.animation)

    implementation (Compose.runtime)
    implementation (Compose.runtimeLivedata)

    implementation ("androidx.navigation:navigation-compose:2.4.0-beta02")



    implementation ("androidx.activity:activity-ktx:1.3.1")
    implementation ("androidx.core:core-ktx:1.7.0-alpha01")
    //implementation ("androidx.activity:activity-compose:1.3.1")
    implementation(AndroidX.Activity.activityCompose)

    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.3.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation ("androidx.window:window:1.0.0-beta03")
    testImplementation("junit:junit:4.12")

    androidTestImplementation ("androidx.test:rules:1.4.0")
    androidTestImplementation ("androidx.test:runner:1.4.0")
    androidTestImplementation (Compose.test)
    androidTestImplementation (Compose.uiTest)


    debugImplementation (Compose.uiTestManifest)
    //implementation "jp.wasabeef.composable:glide:1.0.1"

    implementation ("com.google.accompanist:accompanist-pager:+")

    // If using indicators, also depend on
    implementation ("com.google.accompanist:accompanist-pager-indicators:+")
    implementation ("com.google.accompanist:accompanist-glide:+")
   // implementation "com.google.accompanist:accompanist-coil:+"
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.0-beta02")
    implementation ("com.squareup.retrofit2:retrofit:+")
    implementation ("com.google.accompanist:accompanist-flowlayout:+")
    implementation ("androidx.navigation:navigation-compose:+")
    implementation ("androidx.navigation:navigation-fragment-ktx:+")
    implementation ("androidx.navigation:navigation-ui-ktx:+")
    implementation ("io.coil-kt:coil-compose:1.3.2")


}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
    kotlinOptions {
        // Set JVM target to 1.8
        jvmTarget = Jdk.kotlinJvmTarget
    }
}

