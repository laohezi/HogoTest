
import com.example.bui.*

plugins {
    id("com.android.application")
    id("kotlin-android")
}




android {

    buildToolsVersion = "33"
   // buildSdkVersion = 30
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.hogotest"
        minSdk = 24
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
    }


    // Set both the Java and Kotlin compilers to target Java 8.

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.version

    }

    flavorDimensions += listOf("chanel","country")
    productFlavors {
       register("xiaomi"){
           dimension = "chanel"
           buildConfigField("String","name","xiaomi")

       }
        register("huawei"){
            dimension = "chanel"
        }

        register("us"){
            dimension = "country"
        }
        register("uk"){
            dimension = "country"
        }
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

    buildFeatures {
        viewBinding = true


    }
    applicationVariants.all {

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
    // fileTree("dir" to "libs","include" to  )
    implementation(Kotlin.stdlib)
    implementation(AndroidX.coreKtx)
    implementation(Coroutines.core)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.constrainLayout)
    implementation(AndroidX.RecyclerView.recyclerView)

    implementation(Glide.glide)
    implementation(Glide.compiler)
  //  implementation(Glide.okhttp3Integration)
    //implementation(Test.junit)
    implementation(AndroidX.Test.Ext.junit)

    implementation(Coroutines.android)
    implementation(Compose.ui)
    implementation (Compose.foundation)
    implementation (Compose.material)
    implementation (Compose.materialIconsExtended)
    implementation (Compose.tooling)
    implementation(Compose.animation)
    implementation (Compose.runtime)
    implementation (Compose.runtimeLivedata)
    implementation(AndroidX.Activity.activityCompose)
    debugImplementation(LeakCannary)
    implementation(project(":nativelib"))
    implementation(Glide.glide)
    implementation(com.example.bui.BRVAH)
   // implementation(AndroidX)
    implementation(Okhttp.okhttp)
    implementation(AndroidX.activityKtx)



}
/*implementation(project(':flutter'),{
        exclude group: 'com.android.support'
    })*//*


}*/
