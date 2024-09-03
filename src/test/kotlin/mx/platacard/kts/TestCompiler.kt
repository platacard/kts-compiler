package mx.platacard.kts

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class TestCompiler {
    @Test
    fun testSuccess() {
        val successFile = File("src/test/resources/mx/platacard/kts/success.main.kts")
        val result = KtsCompiler.compileSync(successFile)
        assertEquals(Result.Success, result)
    }

    @Test
    fun testFailure() {
        val successFile = File("src/test/resources/mx/platacard/kts/failed.main.kts")
        val result = KtsCompiler.compileSync(successFile)
        assertTrue { result is Result.Failure }
    }
}
