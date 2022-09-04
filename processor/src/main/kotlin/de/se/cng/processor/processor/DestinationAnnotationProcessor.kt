package de.se.cng.processor.processor

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import de.se.cng.annotation.Destination
import de.se.cng.processor.exceptions.UnsupportedParameterTypeException
import de.se.cng.processor.generator.generateNavigatorFile
import de.se.cng.processor.generator.generateSetupFile
import de.se.cng.processor.generator.generateStubNavigatorFile
import de.se.cng.processor.generator.generateStubSetupFile
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.visitors.FunctionDeclarationVisitor

class DestinationAnnotationProcessor(
    private val options: Map<String, String>,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    companion object {
        const val PACKAGE = "de.se.cng.generated"
    }

    private val functionDeclarationVisitor = FunctionDeclarationVisitor(logger)
    private val destinations = mutableListOf<NavigationDestination>()
    private val allDestinationNames = mutableSetOf<String>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbolsWithAnnotation = resolver.getSymbolsWithAnnotation(Destination::class.qualifiedName!!)
        val unableToProcess = symbolsWithAnnotation.filterNot {
            it.validate()
        }

        symbolsWithAnnotation
            .filter { it.validate() && it is KSFunctionDeclaration }
            .forEach {
                val navigationDestinationResult = functionDeclarationVisitor.visitFunctionDeclaration(it as KSFunctionDeclaration, Unit)
                destinations += navigationDestinationResult
            }

        symbolsWithAnnotation
            .filter { it is KSFunctionDeclaration }
            .forEach {
                allDestinationNames += (it as KSFunctionDeclaration).simpleName.asString()
            }

        logger.info("Processed the following files: ${symbolsWithAnnotation.joinToString { it.containingFile.toString() }}")
        logger.info("Warning while processing the following files: ${unableToProcess.joinToString { it.containingFile.toString() }}")

        return unableToProcess.toList()
    }

    override fun finish() {
        val enableLogging: Boolean = options.getOrDefault("logging", false) as Boolean

        if (validateDestinations()) {
            writeKotlinFiles(enableLogging)
        } else {
            writeStubFiles(enableLogging)
        }
    }

    private fun validateDestinations(): Boolean {
        if (destinations.isEmpty()) {
            logger.info("No destinations where detected.")
            return false
        }

        if (destinations.singleOrNull { it.isHome } == null) {
            logger.warn("No home destination was declared. One @Destination function must be declared as @Home.")
            return false
        }
        return true
    }

    private fun writeKotlinFiles(enableLogging: Boolean): Unit = try {
        val setupFile: FileSpec = generateSetupFile(PACKAGE, destinations, enableLogging)
        val navigatorFile: FileSpec = generateNavigatorFile(PACKAGE, destinations, enableLogging)

        navigatorFile.writeTo(codeGenerator, aggregating = false)
        setupFile.writeTo(codeGenerator, aggregating = false)
    } catch (e: UnsupportedParameterTypeException) {
        logger.error("Unsupported parameter type: ${e.className}")
    } catch (e: Exception) {
        logger.error("Unknown error during code generation: ${e.message}")
    }

    private fun writeStubFiles(enableLogging: Boolean) {
        val stubSetupFile = generateStubSetupFile(PACKAGE, enableLogging)
        val stubNavigatorFile = generateStubNavigatorFile(PACKAGE, allDestinationNames, enableLogging)

        stubSetupFile.writeTo(codeGenerator, aggregating = false)
        stubNavigatorFile.writeTo(codeGenerator, aggregating = false)
    }
}

