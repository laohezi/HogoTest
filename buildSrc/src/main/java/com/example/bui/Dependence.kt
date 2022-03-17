package com.example.bui

object JetPacks{


    object Compose {
        const val snapshot = ""
        const val version = "1.1.0-rc03"

        const val foundation = "androidx.compose.foundation:foundation:$version"
        const val animation = "androidx.compose.animation:animation:$version"
        /*const val layout = "androidx.compose.foundation:foundation-layout:$version"*/
        const val material = "androidx.compose.material:material:$version"
        const val materialIconsExtended = "androidx.compose.material:material-icons-extended:$version"
        const val runtime = "androidx.compose.runtime:runtime:$version"
        const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"
        const val ui = "androidx.compose.ui:ui:$version"
        const val test = "androidx.compose.ui:ui-test:$version"
        const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"
        const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$version"
        /*const val uiUtil = "androidx.compose.ui:ui-util:$version"*/
/*
        const val viewBinding = "androidx.compose.ui:ui-viewbinding:$version"
*/

        object Material3 {
            const val snapshot = ""
            const val version = "1.0.0-alpha02"

            const val material3 = "androidx.compose.material3:material3:$version"
        }
    }

    object Test{
        const val junit = "junit:junit:4.12"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.4.1"
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
        const val constrainLayout = "androidx.constraintlayout:constraintlayout:2.1.3"

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

            object Ext {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
        }

        object Lifecycle {
            private const val version = "2.4.0"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
        }


    }
}

object Kotlin {
    const val version = "1.6.10"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
    const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
}

object Coroutines {
    private const val version = "1.6.0"
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
}
