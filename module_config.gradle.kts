plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    //add ksp plugin
}



dependencies {
    implementation(Hilt.hilt)
    kapt(Hilt.compiler)

}
