
import com.android.tools.build.jetifier.processor.isSignatureFile
import com.example.bui.*

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}




android {

    buildToolsVersion = Sdk.buildToolsVersion
   // buildSdkVersion = 30
    compileSdk = Sdk.compileSdkVersion

    defaultConfig {
        applicationId = "com.example.hogotest"
        minSdk = Sdk.minSdkVersion
        targetSdk = Sdk.targetSdkVersion
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
   signingConfigs {
       val useHome = System.getProperty("user.home")
       val file = File("$useHome/.android/daye.keystore")
      getByName("debug"){
           storeFile = file
           storePassword = "yoxisinei145"
           keyAlias = "daye"
           keyPassword = "yoxisinei145"
       }

//       create("release"){
//           storeFile = file
//           storePassword = "yoxisinei145"
//           keyAlias = "daye"
//           keyPassword = "yoxisinei145"
//       }

   }

    // Set both the Java and Kotlin compilers to target Java 8.

    compileOptions {
        sourceCompatibility = Jdk.sourceCompatibility
        targetCompatibility = Jdk.targetCompatibility
    }

    kotlinOptions {
        jvmTarget = Jdk.kotlinJvmTarget
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
   // implementation(Kotlin.stdlib)
    implementation(AndroidX.coreKtx)
    implementation(Coroutines.core)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.constrainlayout)
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
    androidTestImplementation(AndroidX.Test.runner)

    implementation(com.example.bui.OcrChinese)
    implementation(Camerax.camera2)
    implementation(Camerax.cameraView)
    implementation(Camerax.cameraMlkit)
    implementation(Camerax.lifecycle)
    implementation(Camerax.cameraExtension)
    implementation(Camerax.cameraVideo)
    implementation(Camerax.barcode)
    implementation(UtilsCode)
    implementation (Guava.android)
    implementation(project(":mycommon"))
   // implementation(project(":annotation"))
   // ksp(project(":annotation"))





}
/*implementation(project(':flutter'),{
        exclude group: 'com.android.support'
    })*//*


}*/
