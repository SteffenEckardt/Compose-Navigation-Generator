package de.se.cng.processor.generator

import com.squareup.kotlinpoet.*
import de.se.cng.processor.extensions.pascalcase
import de.se.cng.processor.extensions.requireNotNull
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.models.NavigationParameter

/**
 * TODO:
 *  - Recognize navHostController parameter and autoinject
 */

fun generateSetupFunction(destinations: List<NavigationDestination>): FunSpec = with(FunSpec.builder("SetupNavHost")) {
    val composableAnnotation = ClassName("androidx.compose.runtime", "Composable")
    val navHostController = ClassName("androidx.navigation", "NavHostController")

    addAnnotation(composableAnnotation)
    addParameter("navController", navHostController)
    setupBody(destinations)
    build()
}

/**
 * ```kotlin
 * NavHost(navController = navController, startDestination = "...") {
 *     composable("...", arguments = ...){
 *         ...
 *     }
 * }
 * ```
 */
private fun FunSpec.Builder.setupBody(destinations: List<NavigationDestination>) = with(this) {
    val homeDestination = destinations.singleOrNull { it.isHome }.requireNotNull()
    val homeDestinationRouteTemplate = navRouteTemplate(homeDestination)
    val navHost = MemberName("androidx.navigation.compose", "NavHost")

    addStatement("%M(navController = %L, startDestination = %S)", navHost, "navController", homeDestinationRouteTemplate)
    beginControlFlow("{")
    destinations.forEach { destination ->
        composableCall(destination)
    }
    endControlFlow()
}

/***
 * ```kotlin
 * composable("...", arguments = ...) { backStackEntry ->
 *     Log.d(TAG, "Navigating to destination: \"...\"")
 *     ...()
 * }
 * ```
 */
private fun FunSpec.Builder.composableCall(destination: NavigationDestination) = with(this) {
    val navRouteTemplate = navRouteTemplate(destination)
    val navHost = MemberName("androidx.navigation.compose", "composable")

    if (destination.parameters.isEmpty()) {
        beginControlFlow("%M(%S)", navHost, navRouteTemplate)
    } else {
        addStatement("%M(%S, arguments = listOf(", navHost, navRouteTemplate)
        destination.parameters.forEach { navigationParameter ->
            addNavArgument(navigationParameter)
        }
        beginControlFlow(")) { backStackEntry ->")
        addNavParametersGetters(destination.parameters)
    }

    addLogDebugCall("Navigating to ${destination.actualName}")
    targetCall(destination)
    endControlFlow()
}


private fun FunSpec.Builder.addNavArgument(navigationParameter: NavigationParameter) = with(this) {
    val name = navigationParameter.name
    val isNullable = navigationParameter.isNullable
    val typeName = navigationParameter.typeName
    val typePackage = navigationParameter.typePackage

    val navArgument = MemberName("androidx.navigation", "navArgument")
    addCode(buildCodeBlock {
        withIndent {
            addStatement("%M(\"arg%L\"){", navArgument, name.pascalcase())
            withIndent {
                addStatement("nullable = %L", isNullable)
                addStatement("type = NavType.fromArgType(%S,%S)", typeName, typePackage)
            }
            addStatement("},")
        }
    })
}

private fun navRouteTemplate(navigationDestination: NavigationDestination): String {
    val parameters = navigationDestination.parameters
    val actualName = navigationDestination.actualName

    return when {
        // Simple destination
        parameters.isEmpty()              -> actualName

        // Non-Optional destination
        parameters.none { it.isNullable } -> "$actualName${parameters.joinToString(separator = "/", prefix = "/") { parameter -> "arg${parameter.name.pascalcase()}" }}"

        // Optional destination
        else                              -> "$actualName${
            parameters.joinToString(separator = "&", prefix = "?") { parameter -> "arg${parameter.name.pascalcase()}={${parameter.name}}" }
        }"
    }
}

private fun FunSpec.Builder.addNavParametersGetters(parameters: List<NavigationParameter>) = with(this) {
    fun mapParameterToGetter(packageName: String, simpleName: String) = when ("$packageName.$simpleName") {
        "kotlin.String"  -> "getString"
        "kotlin.Int"     -> "getInt"
        "kotlin.Double"  -> "getDouble"
        "kotlin.Float"   -> "getFloat"
        "kotlin.Boolean" -> "getBoolean"
        else             -> "null" // TODO: Throw exception?
    }

    parameters.forEach { parameter ->
        val parameterName = parameter.name.pascalcase()
        val parameterGetter = mapParameterToGetter(parameter.typePackage, parameter.typeName)

        addStatement("val arg%L = backStackEntry.arguments?.%L(\"arg%L\")", parameterName, parameterGetter, parameterName)
    }
}

private fun FunSpec.Builder.targetCall(destination: NavigationDestination) = with(this) {
    val destinationName = MemberName(destination.actualPackage, destination.actualName)
    val arguments = destination.parameters.joinToString { "${it.name}=arg${it.name.pascalcase()}" }

    addStatement("%M(%L)", destinationName, arguments)
}


private fun FunSpec.Builder.addLogDebugCall(message: String) = with(this) {
    addStatement("Log.d(TAG, \"%L\")", message)
}

