package com.example.bui


object Compose {
    const val snapshot = ""
    const val version = "1.5.6"

    const val bom = "androidx.compose:compose-bom:2023.10.01"

    const val foundation = "androidx.compose.foundation:foundation"
    const val animation = "androidx.compose.animation:animation"

    /*const val layout = "androidx.compose.foundation:foundation-layout"*/
    const val material = "androidx.compose.material:material"
    const val materialIconsExtended = "androidx.compose.material:material-icons-extended"
    const val runtime = "androidx.compose.runtime:runtime"
    const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata"
    const val tooling = "androidx.compose.ui:ui-tooling"
    const val ui = "androidx.compose.ui:ui"
    const val uiTest = "androidx.compose.ui:ui-test-junit4"
    const val uiTestManifest = "androidx.compose.ui:ui-test-manifest"
    /*const val uiUtil = "androidx.compose.ui:ui-util:$version"*/
    /*
            const val viewBinding = "androidx.compose.ui:ui-viewbinding:$version"
    */

    object Material3 {
        const val snapshot = ""
        const val version = "1.0.0-alpha02"

        const val material3 = "androidx.compose.material3:material3"
    }
}

object Test {
    val junit = "junit:junit:4.13.2"
}

object Material {
    private const val version = "1.11.0"
    const val material = "com.google.android.material:material:$version"
}

object Room {
    private const val version = "2.5.1"
    const val runtime = "androidx.room:room-runtime:$version"
    const val compiler = "androidx.room:room-compiler:$version"
    const val ktx = "androidx.room:room-ktx:$version"
    const val paging = "androidx.room:room-paging:$version"
}

object Paging {
    private const val version = "3.1.0-alpha01"
    const val runtime = "androidx.paging:paging-runtime-ktx:$version"
}

object AndroidX {
    const val appcompat = "androidx.appcompat:appcompat:1.7.0-alpha02"
    const val coreKtx = "androidx.core:core-ktx:1.7.0"
    const val constrainlayout = "androidx.constraintlayout:constraintlayout:2.1.3"
    const val activityKtx = "androidx.activity:activity-ktx:1.2.2"

    object Activity {
        const val activityCompose = "androidx.activity:activity-compose:1.4.0"
    }


    object Navigation {
        private const val version = "2.4.0"
        const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
        const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
    }

    object Test {
        private const val version = "1.4.0"
        const val core = "androidx.test:core:$version"
        const val rules = "androidx.test:rules:$version"
        const val runner = "androidx.test:runner:$version"

        object Ext {
            private const val version = "1.1.2"
            const val junit = "androidx.test.ext:junit-ktx:$version"
        }

        const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
    }

    object RecyclerView {
        private val version = "1.2.1"
        val recyclerView = "androidx.recyclerview:recyclerview:$version"
    }

    object Lifecycle {
        private const val version = "2.4.0"
        const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
        const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
    }
}

object Kotlin {
    const val version = "1.9.21"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk17:$version"
    const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    const val reflection = "org.jetbrains.kotlin:kotlin-reflect:$version"
}

object Coroutines {
    private const val version = "1.6.0"
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
}


object Camerax {
    private const val camerax_version = "1.3.0-alpha04"
    val camera2 = "androidx.camera:camera-camera2:${camerax_version}"
    val lifecycle = "androidx.camera:camera-lifecycle:${camerax_version}"
    val cameraVideo = "androidx.camera:camera-video:${camerax_version}"
    val cameraView = "androidx.camera:camera-view:${camerax_version}"
    val cameraMlkit = "androidx.camera:camera-mlkit-vision:${camerax_version}"
    val cameraExtension = "androidx.camera:camera-extensions:${camerax_version}"
    val barcode = "com.google.mlkit:barcode-scanning:17.0.2"


}

object Hilt{
    private  const val version = "2.28-alpha"
    const val hilt = "com.google.dagger:hilt-android:$version"
    const val compiler = "com.google.dagger:hilt-android-compiler:$version"
    const val navigation = "androidx.hilt:hilt-navigation-compose:1.0.0"

}
