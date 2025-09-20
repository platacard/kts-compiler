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
    mainClass.set("mx.platacard.kts.MainKtKt")
}

tasks.shadowJar {
    archiveBaseName.set("kts-compiler")
    archiveClassifier.set("")
    archiveVersion.set("")
    manifest {
        attributes(mapOf("Main-Class" to "mx.platacard.kts.MainKtKt"))
    }
}

tasks.register<JavaExec>("checkAllScripts") {
    group = "verification"
    description = "Check all .main.kts scripts in a directory"
    
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("mx.platacard.kts.MainKtKt")
    
    // Параметры по умолчанию
    val scriptsDir = project.findProperty("scriptsDir")?.toString() ?: "${System.getProperty("user.home")}/Projects/bank/kts"
    
    doFirst {
        println("Checking all .main.kts scripts in: $scriptsDir")
        
        val scriptsDirFile = file(scriptsDir)
        if (!scriptsDirFile.exists()) {
            throw GradleException("Directory does not exist: $scriptsDir")
        }
        
        val scriptFiles = scriptsDirFile.walkTopDown()
            .filter { it.isFile && it.name.endsWith(".main.kts") }
            .toList()
        
        if (scriptFiles.isEmpty()) {
            println("No .main.kts files found in $scriptsDir")
            return@doFirst
        }
        
        println("Found ${scriptFiles.size} script(s) to check:")
        scriptFiles.forEach { println("  - ${it.absolutePath}") }
        
        // Устанавливаем аргументы для main класса
        args = scriptFiles.map { it.absolutePath }
    }
}

tasks.register<JavaExec>("checkAllScriptsDetailed") {
    group = "verification"
    description = "Check all .main.kts scripts with detailed error output"
    
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("mx.platacard.kts.MainKtKt")
    
    // Параметры по умолчанию
    val scriptsDir = project.findProperty("scriptsDir")?.toString() ?: "${System.getProperty("user.home")}/Projects/bank/kts"
    
    doFirst {
        println("=== DETAILED CHECK OF ALL .main.kts SCRIPTS ===")
        println("Directory: $scriptsDir")
        
        val scriptsDirFile = file(scriptsDir)
        if (!scriptsDirFile.exists()) {
            throw GradleException("Directory does not exist: $scriptsDir")
        }
        
        val scriptFiles = scriptsDirFile.walkTopDown()
            .filter { it.isFile && it.name.endsWith(".main.kts") }
            .toList()
        
        if (scriptFiles.isEmpty()) {
            println("No .main.kts files found in $scriptsDir")
            return@doFirst
        }
        
        println("Found ${scriptFiles.size} script(s) to check:")
        scriptFiles.forEach { println("  - ${it.absolutePath}") }
        println()
        
        // Устанавливаем аргументы для main класса
        args = scriptFiles.map { it.absolutePath }
    }
    
    doLast {
        println("\n=== SUMMARY ===")
        println("Check completed. See detailed output above for any errors.")
    }
}
