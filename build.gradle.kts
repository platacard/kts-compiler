plugins {
    kotlin("jvm") version libs.versions.kotlin
    id("maven-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(libs.kts.scripting.common)
    implementation(libs.kts.scripting.jvm)
    implementation(libs.kts.scripting.jvm.host)
    implementation(libs.kts.scripting.jvm.dependencies)
    implementation(libs.apache.ivy)
    implementation(libs.coroutines.core)

    testImplementation(kotlin("test"))
    testImplementation(platform(libs.junit.bom))
    testRuntimeOnly(libs.junit.engine)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "mx.platacard"
            artifactId = "kts-compiler"
            version =
                libs.versions.kts.compiler
                    .get()

            from(components["kotlin"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
java {
    version = 17
}
kotlin {
    jvmToolchain(17)
}
