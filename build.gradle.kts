plugins {
    kotlin("jvm") version libs.versions.kotlin
}

group = "mx.platacard"
version = libs.versions.kts.compiler

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kts.jvm.host)
    implementation(libs.kts.main)
    implementation(libs.coroutines.core)

    testImplementation(kotlin("test"))
    testImplementation(platform(libs.junit.bom))
    testRuntimeOnly(libs.junit.engine)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
