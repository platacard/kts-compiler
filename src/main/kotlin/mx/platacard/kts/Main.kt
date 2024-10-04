package mx.platacard.kts

import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Specify starting directory for scripts")
        exitProcess(-1)
    }
    val startPath = args[0]
    val rootDir = File(startPath)
    val ktsFiles =
        rootDir
            .walkTopDown()
            .filter { it.isFile && it.extension == "kts" }
            .toList()

    val failures: List<Checked<Result.Failure>> =
        ktsFiles
            .map { Checked(it, KtsCompiler.compile(it)) }
            .filter { it.result is Result.Failure }
            .map { Checked(it.file, it.result as Result.Failure) }

    if (failures.isNotEmpty()) {
        println()
        println("Found errors: ${failures.size}")
        println()
        failures.forEach {
            println("    File: ${it.file.path}")
            println()
            println(it.result.formattedMessage)
        }
        println()
        error("Found errors above, stopping execution...")
    }
}

private data class Checked<T>(
    val file: File,
    val result: T,
)
