
import com.example.bui.*

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}




android {

    buildToolsVersion = "34"
   // buildSdkVersion = 30
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hogotest"
        minSdk = 24
        targetSdk = 34
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
        kotlinCompilerExtensionVersion = Compose.version

    }

//    flavorDimensions += listOf("chanel","country")
//    productFlavors {
//       register("xiaomi"){
//           dimension = "chanel"
//           buildConfigField("String","name","xiaomi")
//
//       }
//        register("huawei"){
//            dimension = "chanel"
//        }
//
//        register("us"){
//            dimension = "country"
//        }
//        register("uk"){
//            dimension = "country"
//        }
//    }

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
    // fileTree("dir" to "libs","include" to  )
    implementation(Kotlin.stdlib)
    implementation(AndroidX.coreKtx)
    implementation(Coroutines.core)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.constrainLayout)
    implementation(AndroidX.RecyclerView.recyclerView)

    implementation(Glide.glide)
    /*implementation(Glide.compiler){
        exclude(group = "META-INF/gradle/incremental.annotation.processors")
    }*/
  //  implementation(Glide.okhttp3Integration)
    //implementation(Test.junit)
  //  implementation(AndroidX.Test.Ext.junit)
    val composeBom = platform(Compose.bom)
    implementation(composeBom)
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
    implementation(com.example.bui.AndroidAutoSize)
    implementation(Room.runtime)
    kapt(Room.compiler)
    implementation(Room.paging)
    implementation(Room.ktx)

    implementation(Retrofit.retrofit)
    implementation(com.example.bui.UtilsCode)
    androidTestImplementation("androidx.test:runner:1.5.2")

    implementation(com.example.bui.OcrChinese)
    implementation(Camerax.camera2)
    implementation(Camerax.cameraView)
    implementation(Camerax.cameraMlkit)
    implementation(Camerax.lifecycle)
    implementation(Camerax.cameraExtension)
    implementation(Camerax.cameraVideo)
    implementation(Camerax.barcode)
    implementation(com.example.bui.UtilsCode)
    implementation ("com.google.guava:guava:31.0.1-android")
    implementation(project(":mycommon"))
    implementation(project(":annotation"))
    ksp(project(":annotation"))





}
/*implementation(project(':flutter'),{
        exclude group: 'com.android.support'
    })*//*


}*/
