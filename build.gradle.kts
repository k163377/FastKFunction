plugins {
    kotlin("jvm") version Versions.kotlinVersion apply false
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = Versions.kotlinVersion))
    }
}

group = "com.mapk"
version = Versions.projectVersion
