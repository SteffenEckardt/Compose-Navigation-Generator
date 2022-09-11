package de.se.cng.processor.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import de.se.cng.annotation.Destination
import de.se.cng.processor.exceptions.UnsupportedParameterTypeException
import de.se.cng.processor.generator.navigator.ActualNavigatorClassGenerator
import de.se.cng.processor.generator.navigator.NavigatorClassGeneratorBase
import de.se.cng.processor.generator.navigator.StubNavigatorClassGenerator
import de.se.cng.processor.generator.setup.ActualSetupFunctionGenerator
import de.se.cng.processor.generator.setup.SetupFunctionGeneratorBase
import de.se.cng.processor.generator.setup.StubSetupFunctionGenerator
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.processor.DestinationAnnotationProcessor.GenerationType.*
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
    private val destinations = mutableSetOf<NavigationDestination>()
    private val allDestinationNames = mutableSetOf<String>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbolsWithAnnotation = resolver
            .getSymbolsWithAnnotation(Destination::class.qualifiedName!!)
            .filterIsInstance(KSFunctionDeclaration::class.java)

        symbolsWithAnnotation
            .forEach {
                val navigationDestinationResult = it.accept(functionDeclarationVisitor, Unit)
                destinations += navigationDestinationResult
            }

        symbolsWithAnnotation
            .forEach {
                allDestinationNames += it.simpleName.asString()
            }

        // Default validation seems to have trouble handling navigator parameters.
        // Using custom validation during visit.
        return emptyList()
    }

    override fun finish() {
        val enableLogging: Boolean = options.getOrDefault("logging", false) as Boolean

        when (validateDestinations()) {
            Full  -> writeKotlinFiles(
                navigatorClassGenerator = ActualNavigatorClassGenerator(PACKAGE, destinations, enableLogging),
                setupFunctionGenerator = ActualSetupFunctionGenerator(PACKAGE, destinations, false)
            )
            Error -> writeKotlinFiles(
                navigatorClassGenerator = StubNavigatorClassGenerator(PACKAGE, allDestinationNames, enableLogging),
                setupFunctionGenerator = StubSetupFunctionGenerator(PACKAGE, false)
            )
            None  -> {
                /* no-op */
            }
        }
    }

    private fun validateDestinations(): GenerationType {
        if (destinations.isEmpty()) {
            logger.info("No destinations where detected.")
            return None
        }

        if (destinations.singleOrNull { it.isHome } == null) {
            logger.warn("No home destination was declared. One @Destination function must be declared as @Home.")
            return Error
        }
        return Full
    }

    private fun writeKotlinFiles(navigatorClassGenerator: NavigatorClassGeneratorBase, setupFunctionGenerator: SetupFunctionGeneratorBase): Unit = try {
        val navigatorFile: FileSpec = navigatorClassGenerator.generate()
        val setupFile: FileSpec = setupFunctionGenerator.generate()

        navigatorFile.writeTo(codeGenerator, aggregating = false)
        setupFile.writeTo(codeGenerator, aggregating = false)
    } catch (e: UnsupportedParameterTypeException) {
        logger.error("Unsupported parameter type: ${e.className}")
    } catch (e: Exception) {
        logger.error("Unknown error during code generation: ${e.message}")
    }

    sealed class GenerationType {
        object Full : GenerationType()
        object None : GenerationType()
        object Error : GenerationType()
    }
}

