package mx.platacard.kts.config

import org.junit.jupiter.api.Test
import java.io.File
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CompilerConfigTest {

    @Test
    fun testDefaultConfiguration() {
        val config = CompilerConfig.DEFAULT
        
        assertEquals(Duration.ofMinutes(5), config.timeout)
        assertFalse(config.verbose)
        assertEquals(listOf("https://repo.maven.apache.org/maven2/"), config.repositories)
        assertTrue(config.enableTransitiveDependencies)
        assertTrue(config.forceHttps)
        assertFalse(config.enableConsistencyChecks)
        assertFalse(config.enableValidation)
    }

    @Test
    fun testForCIConfiguration() {
        val config = CompilerConfig.forCI()
        
        assertEquals(Duration.ofMinutes(2), config.timeout)
        assertFalse(config.verbose)
        assertFalse(config.enableConsistencyChecks)
        assertFalse(config.enableValidation)
    }

    @Test
    fun testForDevelopmentConfiguration() {
        val config = CompilerConfig.forDevelopment()
        
        assertTrue(config.verbose)
        assertTrue(config.enableConsistencyChecks)
        assertTrue(config.enableValidation)
    }

    @Test
    fun testWithCacheDir() {
        val customCacheDir = File("/tmp/custom-cache")
        val config = CompilerConfig.DEFAULT.withCacheDir(customCacheDir)
        
        assertEquals(customCacheDir, config.cacheDir)
    }

    @Test
    fun testWithTimeout() {
        val customTimeout = Duration.ofMinutes(10)
        val config = CompilerConfig.DEFAULT.withTimeout(customTimeout)
        
        assertEquals(customTimeout, config.timeout)
    }

    @Test
    fun testWithVerbose() {
        val config = CompilerConfig.DEFAULT.withVerbose(true)
        
        assertTrue(config.verbose)
    }

    @Test
    fun testWithRepositories() {
        val customRepos = listOf("https://custom.repo.com/", "https://another.repo.com/")
        val config = CompilerConfig.DEFAULT.withRepositories(customRepos)
        
        assertEquals(customRepos, config.repositories)
    }

    @Test
    fun testAddRepository() {
        val config = CompilerConfig.DEFAULT.addRepository("https://custom.repo.com/")
        
        assertEquals(2, config.repositories.size)
        assertTrue(config.repositories.contains("https://custom.repo.com/"))
        assertTrue(config.repositories.contains("https://repo.maven.apache.org/maven2/"))
    }

    @Test
    fun testWithTransitiveDependencies() {
        val config = CompilerConfig.DEFAULT.withTransitiveDependencies(false)
        
        assertFalse(config.enableTransitiveDependencies)
    }

    @Test
    fun testWithHttps() {
        val config = CompilerConfig.DEFAULT.withHttps(false)
        
        assertFalse(config.forceHttps)
    }

    @Test
    fun testWithConsistencyChecks() {
        val config = CompilerConfig.DEFAULT.withConsistencyChecks(true)
        
        assertTrue(config.enableConsistencyChecks)
    }

    @Test
    fun testWithValidation() {
        val config = CompilerConfig.DEFAULT.withValidation(true)
        
        assertTrue(config.enableValidation)
    }

    @Test
    fun testChaining() {
        val config = CompilerConfig.DEFAULT
            .withVerbose(true)
            .withTimeout(Duration.ofMinutes(1))
            .addRepository("https://custom.repo.com/")
            .withTransitiveDependencies(false)
        
        assertTrue(config.verbose)
        assertEquals(Duration.ofMinutes(1), config.timeout)
        assertEquals(2, config.repositories.size)
        assertFalse(config.enableTransitiveDependencies)
    }
}
