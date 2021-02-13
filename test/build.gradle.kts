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
    implementation(kotlin("stdlib"))

    implementation(project(":core"))
    kapt(project(":processor"))

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.7.1") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.mockk:mockk:1.10.5")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
