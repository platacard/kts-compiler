import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version libs.versions.kotlin
    application
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

application {
    mainClass.set("mx.platacard.kts.MainKt")
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions.freeCompilerArgs.add("-Xallow-kotlin-package")
}

tasks.jar {
    // archiveClassifier.set("all") // Name the jar as "all" to indicate it's a fat jar

    // Include all dependencies in the jar
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath
            .get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })

    manifest {
        attributes["Main-Class"] = "mx.platacard.kts.MainKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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
