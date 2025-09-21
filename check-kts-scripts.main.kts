#!/usr/bin/env kotlin

/**
 * KTS Scripts Checker for CI/CD
 * 
 * This script runs the kts-compiler JAR to check all .main.kts scripts in a directory.
 * If any script fails to compile, the script exits with non-zero code.
 * 
 * Usage:
 *   kotlinc -script check-kts-scripts.main.kts -- <directory>
 *   kotlinc -script check-kts-scripts.main.kts -- <script-file>
 * 
 * Example:
 *   kotlinc -script check-kts-scripts.main.kts -- /path/to/scripts
 *   kotlinc -script check-kts-scripts.main.kts -- script.main.kts
 */

import java.io.File

println("=== KTS Scripts Checker ===")

if (args.isEmpty()) {
    println("Usage: kotlinc -script check-kts-scripts.main.kts -- <directory-or-file>")
    println("Example: kotlinc -script check-kts-scripts.main.kts -- /path/to/scripts")
    println("Example: kotlinc -script check-kts-scripts.main.kts -- script.main.kts")
    System.exit(1)
}

val inputPath = args[0]
val inputFile = File(inputPath)

if (!inputFile.exists()) {
    println("Error: Path '$inputPath' does not exist")
    System.exit(1)
}

val jarPath = "build/libs/kts-compiler.jar"
val jarFile = File(jarPath)

if (!jarFile.exists()) {
    println("Error: JAR file not found at '$jarPath'")
    println("Please run './gradlew shadowJar' first to build the JAR")
    System.exit(1)
}

println("JAR: $jarPath")
println("Target: $inputPath")
println()

// Create temporary file for output
val tempFile = File.createTempFile("kts-compiler-output", ".txt")

try {
    val processBuilder = ProcessBuilder(
        "java",
        "-Dkotlin.script.classpath=$jarPath",
        "-jar", jarPath,
        inputPath
    )
    
    // Redirect both stdout and stderr to temporary file
    processBuilder.redirectOutput(tempFile)
    processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(tempFile))
    
    val process = processBuilder.start()
    val exitCode = process.waitFor()
    
    // Read output from temporary file
    val output = tempFile.readText()
    println(output)
    
    // Analyze output for errors
    val hasErrors = output.contains("❌ Failed: ") && output.contains("❌ Failed: 0").not()
    
    if (exitCode == 0 && !hasErrors) {
        println("✅ All scripts compiled successfully!")
        System.exit(0)
    } else {
        println("❌ Script compilation failed!")
        if (exitCode != 0) {
            println("Exit code: $exitCode")
        }
        if (hasErrors) {
            println("Errors detected in output")
        }
        System.exit(1)
    }
    
} catch (e: Exception) {
    println("Error running kts-compiler: ${e.message}")
    System.exit(1)
} finally {
    // Clean up temporary file
    if (tempFile.exists()) {
        tempFile.delete()
    }
}
