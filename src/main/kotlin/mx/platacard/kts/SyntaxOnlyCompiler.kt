package mx.platacard.kts

import java.io.File
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.util.isError
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

object SyntaxOnlyCompiler {
    fun compile(scriptFile: File): Result {
        return try {
            val scriptSource = scriptFile.toScriptSource()
            
            // Простая конфигурация только для проверки синтаксиса
            val config = ScriptCompilationConfiguration {
                defaultImports()
                jvm {
                    dependenciesFromCurrentContext(wholeClasspath = true)
                }
                ide {
                    acceptedLocations(ScriptAcceptedLocation.Everywhere)
                }
            }
            
            val result = BasicJvmScriptingHost().compileScript(scriptSource, config)
            
            when {
                result.isError().not() -> Result.Success
                else -> {
                    val errors = result.reports.filter { report ->
                        report.severity == ScriptDiagnostic.Severity.ERROR || report.severity == ScriptDiagnostic.Severity.FATAL
                    }
                    val message = errors.joinToString(separator = "\n") { report ->
                        report.render(withStackTrace = true)
                    }
                    Result.Failure(
                        formattedMessage = message,
                        errors = errors.mapNotNull { report -> report.exception }
                    )
                }
            }
        } catch (e: Exception) {
            Result.Failure(
                formattedMessage = "Syntax check failed: ${e.message}",
                errors = listOf(e)
            )
        }
    }
}
