plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}


dependencies {
    implementation(kotlin("stdlib", "1.9.21"))
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.9.21")


    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.21-1.0.15")
  //  implementation ("com.google.devtools.ksp:symbol-processing-extensions:1.9.21-1.0.15")


    // ASM
    implementation("org.ow2.asm:asm:9.3")
    implementation("org.ow2.asm:asm-commons:9.3")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}