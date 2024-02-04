import com.example.bui.Jdk
import com.example.bui.Kotlin

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}



dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib", "1.8.20"))
    implementation (Kotlin.reflection)
    implementation("com.android.tools.build:gradle-api:8.2.1")

    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.20-1.0.11")
  //  implementation ("com.google.devtools.ksp:symbol-processing-extensions:1.9.21-1.0.15")


    // ASM
    implementation("org.ow2.asm:asm:9.3")
    implementation("org.ow2.asm:asm-commons:9.3")
}

java {
    sourceCompatibility = Jdk.sourceCompatibility
    targetCompatibility = Jdk.targetCompatibility
}