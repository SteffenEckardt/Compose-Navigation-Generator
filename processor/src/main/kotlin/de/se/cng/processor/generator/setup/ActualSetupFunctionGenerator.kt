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

/**
 * Generates a file named _NavHost.kt_, containing the `SetupNavHost(...)` function,
 * which sets up the navigation graph for compose navigation.
 *
 * ```kotlin
 * package de.se.cng.generated

 * import ...
 *
 * @Composable
 * public fun SetupNavHost(navHostController: NavHostController): Unit {
 *  NavHost(navController = navHostController, startDestination = "HomeScreen") {
 *      composable("HomeDestination") {
 *           NoArguments(navigator=Navigator(navHostController))
 *      }
 *      composable("DetailDestination/{argName1}", arguments = listOf(
 *          navArgument("argName1"){
 *              nullable = false
 *              type = NavType.StringType
 *          },
 *      )) { backStackEntry ->
 *          val argName1 = backStackEntry.arguments?.getString("argName1")
 *          requireNotNull(argName1)
 *          SingleNonNullableArgument(navigator=Navigator(navHostController),name1=argName1)
 *      }
 * }
 * ```
 *
 * @param package Package of the generated file.
 * @param destinations Unique set of navigation destinations to include in the nav graph.
 * @param enableLogging If enabled, additional calls to the android logger are added, detailing the workflow steps within the setup function.
 *
 * @return The generator class for the setup function.
 */
class ActualSetupFunctionGenerator(`package`: String, destinations: Set<NavigationDestination>, enableLogging: Boolean) :
    SetupFunctionGeneratorBase(`package`, destinations, enableLogging) {

    /**
     * Generates function body for the setup extension function. It includes all composable calls,
     * as well as their according navArg bocks.
     *
     * ```kotlin
     * NavHost(navController = navController, startDestination = "...") {
     *     composable("...", arguments = ...){
     *         ...
     *     }
     * }
     * ```
     * @param destinations Set of unique navigation destinations to setup.
     * @return [FunSpec.Builder] to continue the builder chain.
     */
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

    /**
     * Adds a composable call within the setup block. The call takes the (generated) route,
     * including arguments, and a list of NavArgs, which define the latter, as parameter.
     * If logging is enabled, an `android.util.Log.d("file_name", ...")` call is added.
     *
     * ```kotlin
     * composable("ROUTE", arguments = ...) { backStackEntry ->
     *     Log.d("file1", "Navigating to destination: \"...\"")
     *     ...()
     * }
     * ```
     * @param destination Specific destination to compose.
     * @return [FunSpec.Builder] to continue the builder chain.
     */
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


    /**
     * Adds a navArgument block to the list of navigation arguments for the specific destination.
     * Each block is called with the argument name and optionally defines one or multiple of the
     * following parameters:
     *  - **nullable**: [Boolean] Whether the argument can be omitted.
     *  - **type**: androidx.navigation.NavType of the navigation argument.
     *  - **hasDefault**: _Not yet possible_
     *
     * ```kotlin
     * navArgument("argName"){
     *      nullable = ...
     *      type = ...
     *      hasDefault = ...
     * }
     * ```
     * @param navigationParameter Specific parameter to map.
     * @return [FunSpec.Builder] to continue the builder chain.
     */
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

    /**
     * Creates the route to a specific destination with **placeholders** for arguments.
     *
     * If the destination:
     * - has no parameters, a simple route is returned.
     * - **only** has non-nullable parameters, a non-nullable route-template is generated.
     * - one or more nullable parameters, a nullable route-template is generated.
     *
     * ```
     *       Simple: "HomeDestination"
     * Non-Nullable: "HomeDestination/{arg1}/{arg2}/{arg3}/..."
     *     Nullable: "HomeDestination?arg1={arg1}&arg2={arg2}&arg3={arg3}&..."
     * ```
     *
     * @param navigationDestination Destination
     * @return [String] Route template with placeholders
     */
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

    /**
     * Adds the deserialization statements for the navigation parameters.
     * Non-nullable parameters are asserted to be non-null by passing them each
     * to requireNonNull().
     *
     * ```kotlin
     * val argName = backStackEntry.arguments?.getString("argName")
     * val argAge = backStackEntry.arguments?.getInt("argAge")
     * val argHeight = backStackEntry.arguments?.getFloat("argHeight")
     *
     * requireNotNull(argName)
     * requireNotNull(argAge)
     * requireNotNull(argHeight)
     * ```
     * @param parameters Set of parameters to retrieve the passed values for.
     * @return [FunSpec.Builder] to continue the builder chain.
     */
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

    /**
     * Adds the call to the actual composable, passing the deserialized parameters.
     * All parameters are explicitly named to prevent errors, should the order of
     * the arguments change for some reason.
     *
     * ```kotlin
     * HomeDestination(arg1="hello", arg2="world", 12345, null, ...)
     * ```
     * @param destination Destination to call.
     * @return [FunSpec.Builder] to continue the builder chain.
     */
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