plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    buildToolsVersion = libs.versions.buildTools.get()
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.example.jetnews1"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildTypes {
        debug {
            //signingConfig = signingConfigs.getByName("debug")
        }

        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packagingOptions {
        resources {
            excludes += setOf("/META-INF/AL2.0", "/META-INF/LGPL2.1")
        }
    }

    namespace = "com.example.app1"
}

dependencies {
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.bundles.androidx.navigation)
    implementation(libs.retrofit)

    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.androidx.test)
    debugImplementation(libs.compose.ui.test.manifest)

    // Accompanist libraries
    implementation("com.google.accompanist:accompanist-pager:+")
    implementation("com.google.accompanist:accompanist-pager-indicators:+")
    implementation("com.google.accompanist:accompanist-glide:+")
    implementation("com.google.accompanist:accompanist-flowlayout:+")

    // Other libraries
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-beta02")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.window:window:1.0.0-beta03")
    implementation("io.coil-kt:coil-compose:1.3.2")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

