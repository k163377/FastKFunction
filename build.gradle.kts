plugins {
    kotlin("jvm") version "1.4.30" apply false
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.4.30"))
    }
}

group = "com.mapk"
version = "0.1.3"
