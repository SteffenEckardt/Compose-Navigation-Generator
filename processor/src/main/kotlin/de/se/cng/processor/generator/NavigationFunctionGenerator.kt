package de.se.cng.processor.generator

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import de.se.cng.processor.extensions.filterNavHostController
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.models.NavigationParameter

private const val FileName = "NavigationFunctions"
private val navigationFunctionImports = listOf(
    Pair("androidx.compose.runtime", "Composable"),
    Pair("androidx.navigation", "NavHostController"),
)

fun generateNavigationExtensionsFile(packageName: String, destinations: List<NavigationDestination>) = with(FileSpec.builder(packageName, FileName))
{
    navigationFunctionImports.forEach { import ->
        addImport(packageName = import.first, import.second)
    }
    destinations.forEach { destination ->
        addFunction(generateNavigationExtensionFunction(destination))
    }
    //addProperty(loggingTag(FileName)) // TODO: Make optional
    build()
}


private fun generateNavigationExtensionFunction(destination: NavigationDestination): FunSpec {
    val functionParameters = destination.parameters
        .filterNavHostController()
        .map { parameter -> navigationFunctionParameters(parameter) }
    val actualName = if (destination.customName.isNullOrEmpty()) destination.actualName else destination.customName

    return with(FunSpec.builder("navigateTo$actualName")) {
        receiver(TypeNames.Classes.NavHostController)
        addNavigationFunctionBody(destination)
        addParameters(functionParameters)
        build()
    }
}

private fun navigationFunctionParameters(parameter: NavigationParameter) = ParameterSpec
    .builder(parameter.name, parameter.className.copy(nullable = parameter.isNullable))
    .build()

private fun FunSpec.Builder.addNavigationFunctionBody(destination: NavigationDestination) = with(this) {
    val navRouteActual = navRouteActual(destination)
    addStatement("navigate(\"%L\")", navRouteActual)
}

private fun navRouteActual(navigationDestination: NavigationDestination): String {
    val parameters = navigationDestination.parameters
        .filterNavHostController()
    val destinationName = navigationDestination.actualName

    return when {
        parameters.isEmpty()              -> destinationName
        parameters.none { it.isNullable } ->
            "$destinationName${parameters.joinToString(separator = "/", prefix = "/") { parameter -> "\$${parameter.name}" }}"
        else                              ->
            "$destinationName${parameters.joinToString(separator = "&", prefix = "?") { parameter -> "arg_${parameter.name}=\$${parameter.name}" }}"
    }
}