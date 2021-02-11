plugins {
    kotlin("jvm")
    kotlin("kapt")
}

group = "com.mapk"
version = Versions.projectVersion

repositories {
    mavenCentral()
}

dependencies {
    // local
    implementation(project(":core"))

    // codegen
    // Kotlinpoet
    implementation("com.squareup:kotlinpoet:${Versions.kotlinpoetVersion}")
    // AutoService
    runtimeOnly("com.google.auto:auto-common:${Versions.autoCommonVersion}")
    implementation("com.google.auto.service:auto-service:${Versions.autoServiceVersion}")
    kapt("com.google.auto.service:auto-service:${Versions.autoServiceVersion}")

    // others
    implementation(kotlin("reflect"))
}
