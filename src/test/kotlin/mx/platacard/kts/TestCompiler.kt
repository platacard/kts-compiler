package mx.platacard.kts

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class TestCompiler {
    @Test
    fun testSuccess() {
        val successFile = File("src/test/resources/mx/platacard/kts/success.main.kts")
        val result = KtsCompiler.compile(successFile)
        assertEquals(Result.Success, result)
    }

    @Test
    fun testFailure() {
        val failedFile = File("src/test/resources/mx/platacard/kts/failed.main.kts")
        val result = KtsCompiler.compile(failedFile)
        assertTrue { result is Result.Failure }
    }

    @Test
    fun testApacheCommonsDependency() {
        val commonsFile = File("src/test/resources/mx/platacard/kts/test-clikt-dependency.main.kts")
        val result = KtsCompiler.compile(commonsFile)
        assertEquals(Result.Success, result)
    }

    @Test
    fun testJsonDependency() {
        val jsonFile = File("src/test/resources/mx/platacard/kts/test-json-dependency.main.kts")
        val result = KtsCompiler.compile(jsonFile)
        assertEquals(Result.Success, result)
    }

    @Test
    fun testMultipleDependencies() {
        val multipleDepsFile = File("src/test/resources/mx/platacard/kts/test-multiple-deps.main.kts")
        val result = KtsCompiler.compile(multipleDepsFile)
        assertEquals(Result.Success, result)
    }

    @Test
    fun testHttpClientDependency() {
        val httpFile = File("src/test/resources/mx/platacard/kts/test-http-client.main.kts")
        val result = KtsCompiler.compile(httpFile)
        assertEquals(Result.Success, result)
    }

    @Test
    fun testMavenCentralFallback() {
        val fallbackFile = File("src/test/resources/mx/platacard/kts/test-maven-central-fallback.main.kts")
        val result = KtsCompiler.compile(fallbackFile)
        assertEquals(Result.Success, result)
    }

    @Test
    fun testPublicDeps() {
        val publicDepsFile = File("src/test/resources/mx/platacard/kts/test-public-deps.main.kts")
        val result = KtsCompiler.compile(publicDepsFile)
        assertEquals(Result.Success, result)
    }

    @Test
    fun testRealScript() {
        val realScriptFile = File("src/test/resources/mx/platacard/kts/test-real-script.main.kts")
        val result = KtsCompiler.compile(realScriptFile)
        assertEquals(Result.Success, result)
    }

    @Test
    fun testTransitive() {
        val transitiveFile = File("src/test/resources/mx/platacard/kts/test-transitive.main.kts")
        val result = KtsCompiler.compile(transitiveFile)
        assertEquals(Result.Success, result)
    }

    @Test
    fun testTransitiveDependencies() {
        val transitiveDepsFile = File("src/test/resources/mx/platacard/kts/test-transitive-dependencies.main.kts")
        val result = KtsCompiler.compile(transitiveDepsFile)
        assertEquals(Result.Success, result)
    }

    @Test
    fun testSpringTransitive() {
        val springTransitiveFile = File("src/test/resources/mx/platacard/kts/test-spring-transitive.main.kts")
        val result = KtsCompiler.compile(springTransitiveFile)
        assertEquals(Result.Success, result)
    }

}
