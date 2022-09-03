package de.se.cng.processor.visitors

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.squareup.kotlinpoet.ksp.toClassName
import de.se.cng.processor.generator.ParameterType
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.models.NavigationParameter

internal class FunctionDeclarationVisitor : KSEmptyVisitor<Unit, NavigationDestination>() {

    override fun defaultHandler(node: KSNode, data: Unit): NavigationDestination {
        TODO("Not yet implemented")
    }

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit): NavigationDestination {
        val destinationName = function.simpleName.asString()
        val destinationPackage = function.packageName.asString()

        val destinationParameters = function.parameters
            .filterNot {
                it.name == null
            }
            .map { outerParameter ->
                val name = outerParameter.name!!.asString()
                val type = outerParameter.type.resolve()
                val parameterType = ParameterType.from(type.toClassName())

                NavigationParameter(name, parameterType, type.isMarkedNullable)
            }
            .toSet()

        return NavigationDestination(actualName = destinationName, actualPackage = destinationPackage, parameters = destinationParameters)
    }
}



