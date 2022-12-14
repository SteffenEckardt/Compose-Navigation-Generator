package de.se.cng.processor.generator.setup

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.withIndent
import de.se.cng.processor.exceptions.UnsupportedParameterTypeException
import de.se.cng.processor.extensions.filterNavigator
import de.se.cng.processor.extensions.pascalcase
import de.se.cng.processor.extensions.requireNotNull
import de.se.cng.processor.generator.ParameterType
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.models.NavigationParameter

class ActualSetupFunctionGenerator(`package`: String, destinations: Set<NavigationDestination>, enableLogging: Boolean) :
    SetupFunctionGeneratorBase(`package`, destinations, enableLogging) {

    override fun FunSpec.Builder.addSetupBody(destinations: Set<NavigationDestination>) = with(this) {
        val homeDestination = destinations.singleOrNull { it.isHome }.requireNotNull()
        val homeDestinationRouteTemplate = navRouteTemplate(homeDestination)
        val navHost = MemberName("androidx.navigation.compose", "NavHost")

        addStatement("%M(navController = %L, startDestination = %S)", navHost, Parameter_NavHostController, homeDestinationRouteTemplate)
        beginControlFlow("{")
        destinations.forEach { destination ->
            addComposableCall(destination)
        }
        endControlFlow()
    }

    override fun FunSpec.Builder.addComposableCall(destination: NavigationDestination) = with(this) {
        val navRouteTemplate = navRouteTemplate(destination)
        val composableCall = MemberName("androidx.navigation.compose", "composable")
        val parameters = destination.parameters.filterNavigator() // Do not treat Navigator as NavArg!

        if (parameters.isEmpty()) {
            beginControlFlow("%M(%S)", composableCall, navRouteTemplate)
        } else {
            addStatement("%M(%S, arguments = listOf(", composableCall, navRouteTemplate)
            parameters.forEach { navigationParameter ->
                addNavArgument(navigationParameter)
            }
            beginControlFlow(")) { backStackEntry ->")
            addNavParametersGetters(parameters)
        }

        logd("Navigating to ${destination.actualName}")
        targetCall(destination)
        endControlFlow()
    }

    private fun FunSpec.Builder.addNavArgument(navigationParameter: NavigationParameter) = with(this) {
        fun navTypeStatement(parameterType: ParameterType): String {
            return when (parameterType) {
                is ParameterType.String  -> "StringType"
                is ParameterType.Int     -> "IntType"
                is ParameterType.Long    -> "LongType"
                is ParameterType.Boolean -> "BoolType"
                is ParameterType.Float   -> "FloatType"
                is ParameterType.Double  -> "FloatType"
                is ParameterType.Byte    -> "IntType"
                is ParameterType.Short   -> "IntType"
                is ParameterType.Char    -> "IntType"
                else                     -> throw UnsupportedParameterTypeException(parameterType)
            }
        }

        val name = navigationParameter.name
        val isNullable = navigationParameter.isNullable

        val navArgument = MemberName("androidx.navigation", "navArgument")
        addCode(buildCodeBlock {
            withIndent {
                addStatement("%M(\"arg%L\"){", navArgument, name.pascalcase())
                withIndent {
                    addStatement("nullable = %L", isNullable)
                    addStatement("type = NavType.%L", navTypeStatement(navigationParameter.type))
                }
                addStatement("},")
            }
        })
    }

    private fun navRouteTemplate(navigationDestination: NavigationDestination): String {
        val actualName = navigationDestination.actualName
        val parameters = navigationDestination.parameters
            .filterNavigator()

        return when {
            // Simple destination
            parameters.isEmpty()              -> actualName

            // Non-Optional destination
            parameters.none { it.isNullable } -> "$actualName${parameters.joinToString(separator = "/", prefix = "/") { parameter -> "{arg${parameter.name.pascalcase()}}" }}"

            // Optional destination
            else                              -> "$actualName${
                parameters.joinToString(separator = "&", prefix = "?") { parameter -> "arg${parameter.name.pascalcase()}={arg${parameter.name.pascalcase()}}" }
            }"
        }
    }

    private fun FunSpec.Builder.addNavParametersGetters(parameters: Set<NavigationParameter>) = with(this) {
        fun mapParameterToGetter(parameterType: ParameterType): String = when (parameterType) {
            is ParameterType.String  -> "getString"
            is ParameterType.Int     -> "getInt"
            is ParameterType.Long    -> "getLong"
            is ParameterType.Boolean -> "getBool"
            is ParameterType.Float   -> "getFloat"
            is ParameterType.Double  -> "getDouble"
            is ParameterType.Byte    -> "getByte"
            is ParameterType.Short   -> "getShort"
            is ParameterType.Char    -> "getChar"
            else                     -> throw UnsupportedParameterTypeException(parameterType)
        }

        parameters.forEach { parameter ->
            val parameterName = parameter.name.pascalcase()
            val parameterGetter = mapParameterToGetter(parameter.type)
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
            .filterNavigator()
            .joinToString { "${it.name}=arg${it.name.pascalcase()}" }

        if (destination.parameters.any { it.type is ParameterType.Navigator }) {
            val navigatorArgName = destination.parameters.single { it.type is ParameterType.Navigator }.name

            if (arguments.isNotEmpty()) {
                addStatement("%M(%L=Navigator(%L),%L)", destinationName, navigatorArgName, Parameter_NavHostController, arguments)
            } else {
                addStatement("%M(%L=Navigator(%L))", destinationName, navigatorArgName, Parameter_NavHostController)
            }
        } else {
            addStatement("%M(%L)", destinationName, arguments)
        }

    }

}