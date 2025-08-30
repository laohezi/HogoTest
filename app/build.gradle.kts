plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

android {
    buildToolsVersion = libs.versions.buildTools.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.hogotest"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.put("room.schemaLocation","$projectDir/schemas".toString())
            }
        }
    }
    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
        dataBinding = false
        viewBinding = true
        aidl = true
    }
   signingConfigs {
       val useHome = System.getProperty("user.home")
       val file = File("$useHome/.android/daye.keystore")
      getByName("debug"){
           storeFile = file
           storePassword = "yoxisinei145"
           keyAlias = "daye"
           keyPassword = "yoxisinei145"
       }
   }

    // Set both the Java and Kotlin compilers to target Java 8.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
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
    namespace = "com.example.hogotest"

    packagingOptions {

    }





    task("My Task"){
        doFirst {
            println("my task do first")
        }
        println("my task")

        doLast {
            println("my task do last")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)

    implementation(libs.glide)

    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.activity.compose)

    debugImplementation(libs.leakcanary.android)
    implementation(libs.glide)
    implementation(libs.brvah)
    implementation(libs.okhttp)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.autosize)

    implementation(libs.bundles.room)
    kapt(libs.room.compiler)

    implementation(libs.retrofit)
    implementation(libs.utilcodex)
    androidTestImplementation(libs.androidx.test.runner)

    implementation(libs.mlkit.text.recognition.chinese)
    implementation(libs.bundles.camerax)
    implementation(libs.mlkit.barcode.scanning)
    implementation(libs.utilcodex)
    implementation(libs.guava.android)

    implementation(project(":mycommon"))
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(project(":nativelib"))
}
