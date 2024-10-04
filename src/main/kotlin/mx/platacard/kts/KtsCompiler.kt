package mx.platacard.kts

import org.jetbrains.kotlin.script.examples.simpleMainKts.COMPILED_SCRIPTS_CACHE_DIR_PROPERTY
import org.jetbrains.kotlin.script.examples.simpleMainKts.SimpleMainKtsScript
import java.io.File
import kotlin.script.experimental.api.CompiledScript
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptDiagnostic.Severity
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.host.BasicScriptingHost
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.util.isError
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

object KtsCompiler {
    private fun BasicScriptingHost.compileScript(
        script: SourceCode,
        compilationConfiguration: ScriptCompilationConfiguration,
    ): ResultWithDiagnostics<CompiledScript> = runInCoroutineContext { compiler(script, compilationConfiguration) }

    private fun compileFile(
        scriptFile: File,
        cacheDir: File? = null,
    ): ResultWithDiagnostics<CompiledScript> =
        withMainKtsCacheDir(cacheDir?.absolutePath ?: "") {
            val scriptDefinition = createJvmCompilationConfigurationFromTemplate<SimpleMainKtsScript>()
            BasicJvmScriptingHost().compileScript(scriptFile.toScriptSource(), scriptDefinition)
        }

    fun compile(scriptFile: File): Result {
        val resultWithDiagnostics = compileFile(scriptFile)

        return when {
            resultWithDiagnostics.isError().not() -> Result.Success
            else -> {
                val results =
                    resultWithDiagnostics.reports.filter {
                        it.severity == Severity.ERROR || it.severity == Severity.FATAL
                    }
                val message =
                    results.joinToString(separator = "\n") {
                        it.render(
                            withStackTrace = true,
                        )
                    }
                Result.Failure(
                    formattedMessage = message,
                    errors = results.mapNotNull { it.exception },
                )
            }
        }
    }

    private fun <T> withMainKtsCacheDir(
        value: String?,
        body: () -> T,
    ): T {
        val prevCacheDir = System.getProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY)
        if (value == null) {
            System.clearProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY)
        } else {
            System.setProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY, value)
        }
        try {
            return body()
        } finally {
            if (prevCacheDir == null) {
                System.clearProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY)
            } else {
                System.setProperty(COMPILED_SCRIPTS_CACHE_DIR_PROPERTY, prevCacheDir)
            }
        }
    }
}
