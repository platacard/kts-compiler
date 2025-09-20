package mx.platacard.kts

import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: kts-compiler <script-file> [script-file2] ...")
        println("Example: kts-compiler script.main.kts")
        System.exit(1)
    }

    var hasErrors = false
    
    for (scriptPath in args) {
        val scriptFile = File(scriptPath)
        
        if (!scriptFile.exists()) {
            println("Error: File '$scriptPath' does not exist")
            hasErrors = true
            continue
        }
        
        if (!scriptFile.isFile) {
            println("Error: '$scriptPath' is not a file")
            hasErrors = true
            continue
        }
        
        println("Compiling: $scriptPath")
        val result = KtsCompiler.compile(scriptFile)
        
        when (result) {
            is Result.Success -> {
                println("✅ Success: $scriptPath")
            }
            is Result.Failure -> {
                println("❌ Error: $scriptPath")
                println(result.formattedMessage)
                hasErrors = true
            }
        }
    }
    
    if (hasErrors) {
        System.exit(1)
    }
}
