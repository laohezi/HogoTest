import com.example.bui.*
plugins {
    id("com.android.application")
    id("kotlin-android")
}




android {
    buildToolsVersion= "32"
    compileSdk = 32
    defaultConfig {
        applicationId = "com.example.jetnews1"
        minSdk= 21
        targetSdk =32
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
        sourceCompatibility =JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = JetPacks.Compose.version
    }

    packagingOptions {
        excludes += "/META-INF/AL2.0"
        excludes += "/META-INF/LGPL2.1"
    }
}

dependencies {
    implementation(Coroutines.android)
    implementation(JetPacks.Compose.ui)
    implementation (JetPacks.Compose.foundation)
    implementation (JetPacks.Compose.material)
    implementation (JetPacks.Compose.materialIconsExtended)
    implementation (JetPacks.Compose.tooling)

    implementation(JetPacks.Compose.animation)

    implementation (JetPacks.Compose.runtime)
    implementation (JetPacks.Compose.runtimeLivedata)

    implementation ("androidx.navigation:navigation-compose:2.4.0-beta02")


    implementation ("androidx.appcompat:appcompat:1.4.0-alpha03")
    implementation ("androidx.activity:activity-ktx:1.3.1")
    implementation ("androidx.core:core-ktx:1.7.0-alpha01")
    implementation ("androidx.activity:activity-compose:1.3.1")

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
    androidTestImplementation (JetPacks.Compose.test)
    androidTestImplementation (JetPacks.Compose.uiTest)


    debugImplementation (JetPacks.Compose.uiTestManifest)
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
        jvmTarget = "1.8"
    }
}

