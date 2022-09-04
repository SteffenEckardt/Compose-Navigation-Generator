package de.se.cng.processor.visitors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.squareup.kotlinpoet.ksp.toClassName
import de.se.cng.annotation.Destination
import de.se.cng.annotation.Home
import de.se.cng.processor.generator.ParameterType
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.models.NavigationParameter

internal class FunctionDeclarationVisitor(private val logger: KSPLogger) : KSEmptyVisitor<Unit, NavigationDestination>() {

    override fun defaultHandler(node: KSNode, data: Unit): NavigationDestination {
        TODO("Not yet implemented")
    }

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit): NavigationDestination {
        val destinationName = function.simpleName.asString()
        val destinationPackage = function.packageName.asString()
        val customName = getAnnotationParameterValue(function, Destination::class.simpleName.toString(), "name")
        val isHome = isAnnotated(function, Home::class.simpleName.toString())

        val destinationParameters = function.parameters
            .filterNot {
                it.name == null
            }
            .map { outerParameter ->
                val name = outerParameter.name?.asString().orEmpty()
                val type = outerParameter.type.resolve()
                val parameterType = ParameterType.from(type.toClassName())

                NavigationParameter(name, parameterType, type.isMarkedNullable)
            }
            .toSet()

        return NavigationDestination(
            actualName = destinationName,
            customName = customName,
            isHome = isHome,
            actualPackage = destinationPackage,
            parameters = destinationParameters
        )
    }

    private fun getAnnotationParameterValue(function: KSFunctionDeclaration, annotationName: String, parameterName: String) = function.annotations
        .single { it.shortName.asString() == annotationName }
        .arguments
        .toList()
        .singleOrNull { it.name?.asString() == parameterName }
        ?.value.toString()

    private fun isAnnotated(function: KSFunctionDeclaration, annotationName: String) = function.annotations
        .any { it.shortName.asString() == annotationName }
}



