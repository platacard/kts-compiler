package mx.platacard.kts

import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: kts-compiler [--syntax-only] <script-file> [script-file2] ...")
        println("Example: kts-compiler script.main.kts")
        println("Example: kts-compiler --syntax-only script.main.kts")
        System.exit(1)
    }

    var hasErrors = false
    var syntaxOnly = false
    val scriptFiles = mutableListOf<String>()
    
    // Parse arguments
    var i = 0
    while (i < args.size) {
        when (args[i]) {
            "--syntax-only" -> {
                syntaxOnly = true
                i++
            }
            else -> {
                scriptFiles.add(args[i])
                i++
            }
        }
    }
    
    if (scriptFiles.isEmpty()) {
        println("Error: No script files specified")
        System.exit(1)
    }
    
    for (scriptPath in scriptFiles) {
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
        
        println("Compiling: $scriptPath${if (syntaxOnly) " (syntax only)" else ""}")
        val result = if (syntaxOnly) {
            SyntaxOnlyCompiler.compile(scriptFile)
        } else {
            KtsCompiler.compile(scriptFile)
        }
        
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
