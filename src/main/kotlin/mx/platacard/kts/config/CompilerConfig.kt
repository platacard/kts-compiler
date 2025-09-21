package mx.platacard.kts.config

import java.io.File
import java.time.Duration

/**
 * Immutable configuration for KTS Compiler.
 * Contains all configurable parameters for compilation and dependency resolution.
 */
data class CompilerConfig(
    /**
     * Cache directory for compilation artifacts.
     * Defaults to system temp directory.
     */
    val cacheDir: File = File(System.getProperty("java.io.tmpdir")),
    
    /**
     * Compilation timeout.
     * Defaults to 5 minutes.
     */
    val timeout: Duration = Duration.ofMinutes(5),
    
    /**
     * Verbose output mode.
     * Defaults to false.
     */
    val verbose: Boolean = false,
    
    /**
     * List of Maven repositories to use for dependency resolution.
     * Defaults to Maven Central.
     */
    val repositories: List<String> = listOf("https://repo1.maven.org/maven2/"),
    
    /**
     * Enable transitive dependency resolution.
     * Defaults to true.
     */
    val enableTransitiveDependencies: Boolean = true,
    
    /**
     * Enable HTTPS for all repositories.
     * Defaults to true.
     */
    val forceHttps: Boolean = true,
    
    /**
     * Enable consistency checks for dependencies.
     * Defaults to false for better compatibility.
     */
    val enableConsistencyChecks: Boolean = false,
    
    /**
     * Enable validation for dependencies.
     * Defaults to false for better compatibility.
     */
    val enableValidation: Boolean = false
) {
    
    /**
     * Creates a copy of this config with updated cache directory.
     */
    fun withCacheDir(cacheDir: File): CompilerConfig = copy(cacheDir = cacheDir)
    
    /**
     * Creates a copy of this config with updated timeout.
     */
    fun withTimeout(timeout: Duration): CompilerConfig = copy(timeout = timeout)
    
    /**
     * Creates a copy of this config with updated verbose mode.
     */
    fun withVerbose(verbose: Boolean): CompilerConfig = copy(verbose = verbose)
    
    /**
     * Creates a copy of this config with updated repositories.
     */
    fun withRepositories(repositories: List<String>): CompilerConfig = copy(repositories = repositories)
    
    /**
     * Creates a copy of this config with additional repository.
     */
    fun addRepository(repository: String): CompilerConfig = copy(repositories = repositories + repository)
    
    /**
     * Creates a copy of this config with updated transitive dependencies setting.
     */
    fun withTransitiveDependencies(enabled: Boolean): CompilerConfig = 
        copy(enableTransitiveDependencies = enabled)
    
    /**
     * Creates a copy of this config with updated HTTPS setting.
     */
    fun withHttps(forceHttps: Boolean): CompilerConfig = copy(forceHttps = forceHttps)
    
    /**
     * Creates a copy of this config with updated consistency checks setting.
     */
    fun withConsistencyChecks(enabled: Boolean): CompilerConfig = 
        copy(enableConsistencyChecks = enabled)
    
    /**
     * Creates a copy of this config with updated validation setting.
     */
    fun withValidation(enabled: Boolean): CompilerConfig = copy(enableValidation = enabled)
    
    companion object {
        /**
         * Default configuration instance.
         */
        val DEFAULT = CompilerConfig()
        
        /**
         * Creates a configuration for CI/CD environments.
         * Optimized for speed and reliability.
         */
        fun forCI(): CompilerConfig = CompilerConfig(
            timeout = Duration.ofMinutes(2),
            verbose = false,
            enableConsistencyChecks = false,
            enableValidation = false
        )
        
        /**
         * Creates a configuration for development environments.
         * Optimized for debugging and detailed output.
         */
        fun forDevelopment(): CompilerConfig = CompilerConfig(
            verbose = true,
            enableConsistencyChecks = true,
            enableValidation = true
        )
    }
}
