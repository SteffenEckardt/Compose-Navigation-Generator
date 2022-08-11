package composektx.navigationgenerator.processor.generator

import androidx.navigation.NavHost
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import composektx.navigationgenerator.processor.models.NavigationDestination
import composektx.navigationgenerator.processor.models.NavigationParameter

fun generateNavigationExtensionFunction(destination: NavigationDestination): FunSpec {
    val functionParameters = destination.parameters.map { parameter -> navigationFunctionParameters(parameter) }
    val actualName = if (destination.customName.isNullOrEmpty()) destination.actualName else destination.customName

    return with(FunSpec.builder("navigateTo$actualName")) {
        receiver(NavHost::class)
        addNavigationFunctionBody(destination)
        addParameters(functionParameters)
        build()
    }
}

private fun navigationFunctionParameters(parameter: NavigationParameter) = ParameterSpec
    .builder(parameter.name, ClassName(parameter.typePackage, parameter.typeName).copy(nullable = parameter.isNullable))
    .build()

private fun FunSpec.Builder.addNavigationFunctionBody(destination: NavigationDestination) = with(this) {
    val navRouteActual = navRouteActual(destination)
    addStatement("navController.navigate(\"%L\")", navRouteActual)
}

private fun navRouteActual(navigationDestination: NavigationDestination): String {
    val parameters = navigationDestination.parameters
    val destinationName = navigationDestination.actualName

    return when {
        parameters.isEmpty()              -> destinationName
        parameters.none { it.isNullable } ->
            "$destinationName${parameters.joinToString(separator = "/", prefix = "/") { parameter -> "\$${parameter.name}" }}"
        else                              ->
            "$destinationName${parameters.joinToString(separator = "&", prefix = "?") { parameter -> "arg_${parameter.name}=\$${parameter.name}" }}"
    }
}