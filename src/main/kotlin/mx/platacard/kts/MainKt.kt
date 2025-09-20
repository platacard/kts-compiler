package mx.platacard.kts

import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: kts-compiler <script-file> [script-file2] ...")
        println("       kts-compiler <directory>")
        println("Example: kts-compiler script.main.kts")
        println("Example: kts-compiler /path/to/scripts")
        System.exit(1)
    }

    val inputPath = args[0]
    val inputFile = File(inputPath)
    
    if (!inputFile.exists()) {
        println("Error: Path '$inputPath' does not exist")
        System.exit(1)
    }
    
    val scriptFiles = if (inputFile.isDirectory) {
        // Если это папка, ищем все .main.kts файлы
        inputFile.walkTopDown()
            .filter { it.isFile && it.name.endsWith(".main.kts") }
            .toList()
    } else {
        // Если это файл, используем его
        listOf(inputFile)
    }
    
    if (scriptFiles.isEmpty()) {
        println("No .main.kts files found in '$inputPath'")
        System.exit(1)
    }
    
    println("=== KTS COMPILER REPORT ===")
    println("Found ${scriptFiles.size} script(s) to check")
    println("Directory: ${inputFile.absolutePath}")
    println()
    
    var successCount = 0
    var errorCount = 0
    val failedScripts = mutableListOf<String>()
    
    for (scriptFile in scriptFiles) {
        println("Compiling: ${scriptFile.absolutePath}")
        val result = KtsCompiler.compile(scriptFile)
        
        when (result) {
            is Result.Success -> {
                println("✅ Success: ${scriptFile.name}")
                successCount++
            }
            is Result.Failure -> {
                println("❌ Error: ${scriptFile.name}")
                println(result.formattedMessage)
                failedScripts.add("${scriptFile.absolutePath}: ${result.formattedMessage}")
                errorCount++
            }
        }
        println()
    }
    
    // Генерируем итоговый отчет
    println("=== SUMMARY ===")
    println("Total scripts: ${scriptFiles.size}")
    println("✅ Successful: $successCount")
    println("❌ Failed: $errorCount")
    println("📊 Success rate: ${(successCount * 100 / scriptFiles.size)}%")
    
    if (failedScripts.isNotEmpty()) {
        println()
        println("=== FAILED SCRIPTS ===")
        failedScripts.forEach { println(it) }
    }
    
    if (errorCount > 0) {
        System.exit(1)
    }
}

