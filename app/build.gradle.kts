
import com.example.bui.*

plugins {
    id("com.android.application")
    id("kotlin-android")
}




android {

    buildToolsVersion = "30.0.2"
   // buildSdkVersion = 30
    compileSdk = 31

    defaultConfig {
        applicationId = "com.example.hogotest"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    /*buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
    }*/


    // Set both the Java and Kotlin compilers to target Java 8.

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    /*composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-alpha05"
    }*/

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }
}





dependencies {
    // fileTree("dir" to "libs","include" to  )
    implementation(Kotlin.stdlib)
    implementation(JetPacks.AndroidX.coreKtx)
    implementation(Coroutines.core)
    implementation(JetPacks.AndroidX.appcompat)
    implementation(JetPacks.AndroidX.constrainLayout)

    implementation(ThirdParty.Glide.glide)
    implementation(ThirdParty.Glide.compiler)
    implementation(ThirdParty.Glide.okhttp3Integration)
    implementation(JetPacks.Test.junit)
    implementation(JetPacks.AndroidX.Test.Ext.junit)




}
/*implementation(project(':flutter'),{
        exclude group: 'com.android.support'
    })*//*


}*/
