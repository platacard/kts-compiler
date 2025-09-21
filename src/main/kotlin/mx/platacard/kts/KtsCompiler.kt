package mx.platacard.kts

import java.io.File

/**
 * KTS Compiler - High-level interface for KTS script compilation.
 * Delegates to Detector for actual compilation logic.
 */
object KtsCompiler {
    
    /**
     * Compiles a KTS script file.
     * 
     * @param scriptFile The KTS script file to compile
     * @param cacheDir Optional cache directory for compilation artifacts
     * @return Result.Success if compilation succeeds, Result.Failure with details if it fails
     */
    fun compile(scriptFile: File, cacheDir: File? = null): Result {
        return Detector.detect(scriptFile, cacheDir)
    }
    
    /**
     * Compiles multiple KTS script files.
     * 
     * @param scriptFiles List of KTS script files to compile
     * @param cacheDir Optional cache directory for compilation artifacts
     * @return Map of file paths to their compilation results
     */
    fun compileBatch(scriptFiles: List<File>, cacheDir: File? = null): Map<String, Result> {
        return Detector.detectBatch(scriptFiles, cacheDir)
    }
    
    /**
     * Compiles all .main.kts files in a directory.
     * 
     * @param directory Directory to scan for .main.kts files
     * @param cacheDir Optional cache directory for compilation artifacts
     * @return Map of file paths to their compilation results
     */
    fun compileInDirectory(directory: File, cacheDir: File? = null): Map<String, Result> {
        return Detector.detectInDirectory(directory, cacheDir)
    }
}

