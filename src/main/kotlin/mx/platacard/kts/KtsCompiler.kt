package mx.platacard.kts

import kotlinx.coroutines.runBlocking
import org.jetbrains.kotlin.mainKts.MainKtsScript
import java.io.File
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.util.isError
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

object KtsCompiler {
    suspend fun compile(scriptFile: File): Result {
        val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<MainKtsScript>()
        val resultWithDiagnostics =
            BasicJvmScriptingHost().compiler.invoke(
                script = scriptFile.toScriptSource(),
                scriptCompilationConfiguration = compilationConfiguration,
            )

        return when {
            resultWithDiagnostics.isError().not() -> Result.Success
            else -> {
                val message =
                    resultWithDiagnostics.reports.joinToString(separator = "\n") {
                        it.render(
                            withStackTrace = true,
                        )
                    }
                Result.Failure(
                    formattedMessage = message,
                    errors = resultWithDiagnostics.reports.mapNotNull { it.exception },
                )
            }
        }
    }

    fun compileSync(file: File) = runBlocking { compile(file) }
}
