package de.se.cng.processor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import de.se.cng.processor.extensions.deepEquals
import de.se.cng.processor.provider.DestinationAnnotationProcessorProvider
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.io.TempDir
import java.io.File

abstract class ProcessorTestBase {

    @field:TempDir
    lateinit var tempFolder: File

    protected fun compile(vararg source: SourceFile) = KotlinCompilation().apply {
        sources = source.toList()
        symbolProcessorProviders = listOf(DestinationAnnotationProcessorProvider())
        workingDir = tempFolder
        inheritClassPath = true
        verbose = true
    }.compile()

    protected fun assertSourceEquals(@Language("kotlin") expected: String, actual: String) {
        var cleanExpected = expected
            .trim()
            .replace(" ", "")
            .replace("\t", "")
            .replace(regex = Regex("(?m)^[ \t]*\r?\n"), "")
            .trimIndent()
            .lines()
            .joinToString(separator = "\n") { line -> line.trim() };

        var cleanActual = actual
            .trim()
            .replace(" ", "")
            .replace("\t", "")
            .replace(regex = Regex("(?m)^[ \t]*\r?\n"), "")
            .trimIndent()
            .lines()
            .joinToString(separator = "\n") { line -> line.trim() };

        val expectedImports = cleanExpected
            .lines()
            .filter { it.trim().startsWith("import") }
            .map { it.trim() }

        val actualImports = cleanActual
            .lines()
            .filter { it.trim().startsWith("import") }
            .map { it.trim() }

        if (expectedImports.deepEquals(actualImports)) {
            expectedImports.forEach { import ->
                cleanExpected = cleanExpected.replace(import, "")
                cleanActual = cleanActual.replace(import, "")
            }
        }

        if (cleanActual == cleanExpected) {
            assertEquals(cleanExpected, cleanActual)
        } else {
            assertEquals(expected, actual)
        }
    }

    protected fun assertEmpty(collection: Collection<*>) = assertTrue(collection.isEmpty())
    protected fun assertNotEmpty(collection: Collection<*>) = assertTrue(collection.isNotEmpty())


    protected fun KotlinCompilation.Result.sourceFor(fileName: String) = kspGeneratedSources()
        .find { it.name == fileName }
        ?.readText()
        ?: throw IllegalArgumentException("Could not find file $fileName in ${kspGeneratedSources()}")

    private fun KotlinCompilation.Result.kspGeneratedSources(): List<File> {
        val kspWorkingDir = workingDir.resolve("ksp")
        val kspGeneratedDir = kspWorkingDir.resolve("sources")
        val kotlinGeneratedDir = kspGeneratedDir.resolve("kotlin")
        val javaGeneratedDir = kspGeneratedDir.resolve("java")
        return kotlinGeneratedDir.walk().toList() +
            javaGeneratedDir.walk().toList()
    }


    protected fun Any?.isNull() = this == null
    protected fun Any?.isNotNull() = this != null

    private val KotlinCompilation.Result.workingDir: File
        get() = checkNotNull(outputDirectory.parentFile)
}


