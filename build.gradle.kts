plugins {
    kotlin("jvm") version libs.versions.kotlin
    application
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
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

application {
    mainClass.set("mx.platacard.kts.cli.MainKtKt")
}

tasks.shadowJar {
    archiveBaseName.set("kts-compiler")
    archiveClassifier.set("")
    archiveVersion.set("")
    manifest {
        attributes(mapOf("Main-Class" to "mx.platacard.kts.cli.MainKtKt"))
    }
}

// Configure distributions to include the KTS script
distributions {
    main {
        contents {
            from("check-kts-scripts.main.kts") {
                into("bin")
            }
            from("README.md") {
                into("docs")
            }
            from("DISTRIBUTION-README.md") {
                into(".")
                rename { "README.md" }
            }
        }
    }
}

// Custom task to create a complete distribution with script
tasks.register<Zip>("distWithScript") {
    group = "distribution"
    description = "Creates a distribution ZIP with JAR and KTS script"
    
    archiveBaseName.set("kts-compiler")
    archiveClassifier.set("complete")
    archiveVersion.set("")
    
    from(tasks.shadowJar) {
        into("lib")
    }
    
    from("check-kts-scripts.main.kts") {
        into("bin")
    }
    
    from("README.md") {
        into("docs")
    }
    from("DISTRIBUTION-README.md") {
        into(".")
        rename { "README.md" }
    }
}

