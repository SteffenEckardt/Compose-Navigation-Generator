package de.se.cng.processor.generator

import com.squareup.kotlinpoet.*
import de.se.cng.processor.extensions.pascalcase
import de.se.cng.processor.extensions.requireNotNull
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.models.NavigationParameter

private const val Parameter_NavHostController = "navController"
private const val FileName = "NavHost"
private val navHostImports = listOf(
    Pair("android.util", "Log"),
    Pair("androidx.compose.runtime", "Composable"),
    Pair("androidx.navigation", "NavHostController"),
    Pair("androidx.navigation", "navArgument"),
    Pair("androidx.navigation", "NavType"),
    Pair("androidx.navigation.compose", "NavHost"),
    Pair("androidx.navigation.compose", "composable"),
    Pair("androidx.navigation.compose", "composable"),
)

fun generateSetupFile(`package`: String, destinations: List<NavigationDestination>) = with(FileSpec.builder("de.se.cng", FileName)) {
    navHostImports.forEach { import ->
        addImport(packageName = import.first, import.second)
    }
    addFunction(generateSetupFunction(destinations))
    addProperty(loggingTag(FileName))

    build()
}

private fun generateSetupFunction(destinations: List<NavigationDestination>): FunSpec = with(FunSpec.builder("SetupNavHost")) {
    val composableAnnotation = ClassName("androidx.compose.runtime", "Composable")

    addAnnotation(composableAnnotation)
    addParameter(Parameter_NavHostController, TypeNames.Classes.NavHostController)
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

    addStatement("%M(navController = %L, startDestination = %S)", navHost, Parameter_NavHostController, homeDestinationRouteTemplate)
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
    val parameters = destination.parameters.filterNot { it.className == TypeNames.Classes.NavHostController } // Do not treat NavHostController as NavArg!

    if (parameters.isEmpty()) {
        beginControlFlow("%M(%S)", navHost, navRouteTemplate)
    } else {
        addStatement("%M(%S, arguments = listOf(", navHost, navRouteTemplate)
        parameters.forEach { navigationParameter ->
            addNavArgument(navigationParameter)
        }
        beginControlFlow(")) { backStackEntry ->")
        addNavParametersGetters(parameters)
    }

    addLogDebugCall("Navigating to ${destination.actualName}")
    targetCall(destination)
    endControlFlow()
}


private fun FunSpec.Builder.addNavArgument(navigationParameter: NavigationParameter) = with(this) {
    val name = navigationParameter.name
    val isNullable = navigationParameter.isNullable
    val className = navigationParameter.className

    val navArgument = MemberName("androidx.navigation", "navArgument")
    addCode(buildCodeBlock {
        withIndent {
            addStatement("%M(\"arg%L\"){", navArgument, name.pascalcase())
            withIndent {
                addStatement("nullable = %L", isNullable)
                addStatement("type = NavType.fromArgType(%S,%S)", className.simpleName, className.packageName)
            }
            addStatement("},")
        }
    })
}

private fun navRouteTemplate(navigationDestination: NavigationDestination): String {
    val actualName = navigationDestination.actualName
    val parameters = navigationDestination.parameters
        .filterNavHostController()

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
    fun mapParameterToGetter(className: ClassName) = when (className.canonicalName) {
        "kotlin.String"  -> "getString"
        "kotlin.Int"     -> "getInt"
        "kotlin.Double"  -> "getDouble"
        "kotlin.Float"   -> "getFloat"
        "kotlin.Boolean" -> "getBoolean"
        else             -> "null" // TODO: Throw exception?
    }

    parameters.forEach { parameter ->
        val parameterName = parameter.name.pascalcase()
        val parameterGetter = mapParameterToGetter(parameter.className)

        addStatement("val arg%L = backStackEntry.arguments?.%L(\"arg%L\")", parameterName, parameterGetter, parameterName)
    }

    parameters.filterNot { it.isNullable }.forEach { parameter ->
        val parameterName = parameter.name.pascalcase()
        addStatement("requireNotNull(arg%L)", parameterName)
    }
}

private fun FunSpec.Builder.targetCall(destination: NavigationDestination) = with(this) {
    val destinationName = MemberName(destination.actualPackage, destination.actualName)
    val arguments = destination.parameters
        .filterNavHostController()
        .joinToString { "${it.name}=arg${it.name.pascalcase()}" }

    if (destination.parameters.any { it.className == TypeNames.Classes.NavHostController }) {
        val navHostControllerArgName = destination.parameters.single { it.className == TypeNames.Classes.NavHostController }.name
        if (arguments.isNotEmpty()) {
            addStatement("%M(%L=%L,%L)", destinationName, navHostControllerArgName, Parameter_NavHostController, arguments)
        } else {
            addStatement("%M(%L=%L)", destinationName, navHostControllerArgName, Parameter_NavHostController)
        }
    } else {
        addStatement("%M(%L)", destinationName, arguments)
    }

}

private fun FunSpec.Builder.addLogDebugCall(message: String) = with(this) {
    addStatement("Log.d(TAG, \"%L\")", message)
}


private fun Collection<NavigationParameter>.filterNavHostController() = this.filterNot { it.className == TypeNames.Classes.NavHostController }



