@file:OptIn(KspExperimental::class)

package de.se.cng.processor.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
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
import de.se.cng.annotation.Home
import de.se.cng.processor.generator.CommonFunctionsGenerator
import de.se.cng.processor.generator.generateNavigationExtensionFunction
import de.se.cng.processor.generator.generateSetupFunction
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.visitors.FunctionDeclarationVisitor

class DestinationAnnotationProcessor(
    private val options: Map<String, String>,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    companion object {
        val navHostImports = listOf(
            Pair("android.util", "Log"),
            Pair("androidx.compose.runtime", "Composable"),
            Pair("androidx.navigation", "NavHostController"),
            Pair("androidx.navigation", "navArgument"),
            Pair("androidx.navigation", "NavType"),
            Pair("androidx.navigation.compose", "NavHost"),
            Pair("androidx.navigation.compose", "composable"),
            Pair("androidx.navigation.compose", "composable"),
        )

        val navigationFunctionImports = listOf(
            Pair("androidx.compose.runtime", "Composable"),
            Pair("androidx.navigation", "NavHost"),
        )
    }

    private val functionDeclarationVisitor = FunctionDeclarationVisitor()
    private val destinations = mutableListOf<NavigationDestination>()

    override fun process(resolver: Resolver): List<KSAnnotated> {

        val symbolsWithAnnotation = resolver.getSymbolsWithAnnotation(Destination::class.qualifiedName!!)
        val unableToProcess = symbolsWithAnnotation.filterNot {
            it.validate()
        }

        symbolsWithAnnotation
            .filter { it.validate() }
            .mapNotNull {
                if (it is KSFunctionDeclaration) {
                    val destinationAnnotation = it.getAnnotationsByType(Destination::class).first()
                    val hasHomeAnnotation = it.getAnnotationsByType(Home::class).any()
                    val navigationDestination = functionDeclarationVisitor.visitFunctionDeclaration(it, Unit)

                    // Fixme: Find more elegant solution
                    navigationDestination.copy(customName = destinationAnnotation.name, isHome = hasHomeAnnotation)
                } else {
                    null
                }
            }.let { propertySpecSequence ->
                destinations.addAll(propertySpecSequence)
            }

        return unableToProcess.toList()
    }

    override fun finish() {
        if (destinations.isEmpty()) {
            logger.info("No destinations where detected.")
            return
        }

        if (destinations.singleOrNull { it.isHome } == null) {
            logger.error("No home destination was declared. One @Destination function must be declared as @Home.")
            return
        }

        val setupFunction = generateSetupFunction(destinations)
        val navigationFunctions = destinations.map { destination -> generateNavigationExtensionFunction(destination) }

        FileSpec
            .builder("de.todo", "NavHost").apply {
                navHostImports.forEach { import ->
                    addImport(packageName = import.first, import.second)
                }
                addFunction(setupFunction)
                addProperty(CommonFunctionsGenerator.loggingTag("NavHost"))
            }
            .build()
            .writeTo(codeGenerator, true)


        FileSpec
            .builder("de.todo", "NavigationFunctions").apply {
                navigationFunctionImports.forEach { import ->
                    addImport(packageName = import.first, import.second)
                }
                navigationFunctions.forEach { navigationFunction ->
                    addFunction(navigationFunction)
                }
                //addProperty(CommonFunctionsGenerator.loggingTag("NavigationFunctions"))
            }
            .build()
            .writeTo(codeGenerator, true)


    }

    private fun isValidComposeFunctionDeclaration(ksAnnotated: KSAnnotated): Boolean {
        return ksAnnotated is KSFunctionDeclaration && ksAnnotated.validate()
        //&& ksAnnotated.annotations.any { it.shortName == "Composable" } // TODO: Check, if function is compose function
    }

}

