package de.se.cng.processor.generator.navigator

import com.squareup.kotlinpoet.*
import de.se.cng.processor.extensions.filterNavigator
import de.se.cng.processor.extensions.pascalcase
import de.se.cng.processor.generator.TypeNames
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.models.NavigationParameter

class ActualNavigatorClassGenerator(
    packageName: String,
    destinations: Set<NavigationDestination>,
    enableLogging: Boolean = false,
) : NavigatorClassGeneratorBase(packageName, destinations, enableLogging) {

    private val navOptionsBuilderLambda = LambdaTypeName.get(receiver = TypeNames.Classes.NavOptionsBuilder, returnType = UNIT)

    private val navOptionsParameter = ParameterSpec
        .builder("navOptions", navOptionsBuilderLambda.copy(nullable = true))
        .defaultValue("null")
        .build()

    override fun FileSpec.Builder.addNavigatorClass(destinations: Set<NavigationDestination>) = addType(with(TypeSpec.classBuilder(fileName)) {
        addNavHostControllerProperty()
        addConstructor()
        destinations.forEach { destination ->
            addNavigationFunction(destination)
        }
        addNavigateHomeFunction(destinations)
        navigateUpFunction()
        build()
    })

    override fun TypeSpec.Builder.addNavigationFunction(destination: NavigationDestination, specialName: String?): TypeSpec.Builder {
        val actualName = if (destination.customName.isNullOrEmpty()) destination.actualName else destination.customName
        val name = specialName ?: "navigateTo$actualName"

        return addFunction(FunSpec.builder(name)
            .addNavigationFunctionBody(destination)
            .addNavigationFunctionParameters(destination.parameters.filterNavigator())
            .addParameter(navOptionsParameter)
            .build()
        )
    }

    override fun TypeSpec.Builder.addNavigateHomeFunction(destinations: Set<NavigationDestination>) =
        addNavigationFunction(destinations.single { it.isHome }, specialName = "navigateHome")

    override fun FunSpec.Builder.addNavigationFunctionParameters(parameters: Set<NavigationParameter>) = addParameters(
        parameters.map { parameter ->
            ParameterSpec
                .builder(parameter.name, parameter.type.className.copy(nullable = parameter.isNullable))
                .build()
        }
    )

    private fun FunSpec.Builder.addNavigationFunctionBody(destination: NavigationDestination) =
        addStatement("navHostController.navigate(\"%L\", navOptions ?: { })", navRouteActual(destination))

    private fun navRouteActual(navigationDestination: NavigationDestination): String {
        val parameters = navigationDestination.parameters.filterNavigator()
        val destinationName = navigationDestination.actualName

        return when {
            parameters.isEmpty()              -> destinationName
            parameters.none { it.isNullable } -> "$destinationName${parameters.joinToString(separator = "/", prefix = "/") { parameter -> "\$${parameter.name}" }}"
            else                              -> "$destinationName${
                parameters.joinToString(separator = "&", prefix = "?") { parameter -> "arg${parameter.name.pascalcase()}=\$${parameter.name}" }
            }"
        }
    }
}



