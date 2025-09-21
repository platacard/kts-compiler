package mx.platacard.kts

import org.jetbrains.kotlin.script.examples.simpleMainKts.COMPILED_SCRIPTS_CACHE_DIR_PROPERTY
import org.jetbrains.kotlin.script.examples.simpleMainKts.SimpleMainKtsScript
import java.io.File
import kotlin.script.experimental.api.CompiledScript
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptDiagnostic.Severity
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.util.isError
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate
import kotlinx.coroutines.runBlocking

/**
 * Detector for KTS script compilation.
 * Provides a clean interface for detecting compilation issues in Kotlin Script files.
 */
object Detector {
    
    /**
     * Detects compilation issues in a KTS script file.
     * 
     * @param scriptFile The KTS script file to analyze
     * @param cacheDir Optional cache directory for compilation artifacts
     * @return Result.Success if compilation succeeds, Result.Error with details if it fails
     */
    fun detect(scriptFile: File, cacheDir: File? = null): Result {
        return try {
            val compilationResult = compileScript(scriptFile, cacheDir)
            
            if (!compilationResult.isError()) {
                Result.Success
            } else {
                val errorReports = compilationResult.reports.filter { 
                    it.severity == Severity.ERROR || it.severity == Severity.FATAL 
                }
                val errorMessage = errorReports.joinToString("\n") { report ->
                    "ERROR ${report.message} (${scriptFile.name}:${report.location?.start?.line ?: "unknown"})"
                }
                Result.Failure(
                    formattedMessage = errorMessage,
                    errors = errorReports.mapNotNull { it.exception }
                )
            }
        } catch (e: Exception) {
            Result.Failure(
                formattedMessage = "Compilation failed: ${e.message}",
                errors = listOf(e)
            )
        }
    }
    
    /**
     * Internal method to compile a script and return detailed diagnostics
     */
    private fun compileScript(
        scriptFile: File,
        cacheDir: File?
    ): ResultWithDiagnostics<CompiledScript> {
        return withMainKtsCacheDir(cacheDir?.absolutePath ?: "") {
            val scriptDefinition = createJvmCompilationConfigurationFromTemplate<SimpleMainKtsScript>()
            val host = BasicJvmScriptingHost()
            
            runBlocking {
                host.compiler(scriptFile.toScriptSource(), scriptDefinition)
            }
        }
    }
    
    /**
     * Helper method for cache directory management
     */
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
    
    /**
     * Batch detection for multiple script files
     * 
     * @param scriptFiles List of KTS script files to analyze
     * @param cacheDir Optional cache directory for compilation artifacts
     * @return Map of file paths to their detection results
     */
    fun detectBatch(scriptFiles: List<File>, cacheDir: File? = null): Map<String, Result> {
        return scriptFiles.associate { file ->
            file.absolutePath to detect(file, cacheDir)
        }
    }
    
    /**
     * Detects issues in all .main.kts files in a directory
     * 
     * @param directory Directory to scan for .main.kts files
     * @param cacheDir Optional cache directory for compilation artifacts
     * @return Map of file paths to their detection results
     */
    fun detectInDirectory(directory: File, cacheDir: File? = null): Map<String, Result> {
        val scriptFiles = directory.walkTopDown()
            .filter { it.isFile && it.name.endsWith(".main.kts") }
            .toList()
        
        return detectBatch(scriptFiles, cacheDir)
    }
}
