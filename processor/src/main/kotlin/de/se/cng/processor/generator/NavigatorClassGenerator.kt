package de.se.cng.processor.generator

import com.squareup.kotlinpoet.*
import de.se.cng.processor.extensions.filterNavigator
import de.se.cng.processor.extensions.pascalcase
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.models.NavigationParameter

private const val FileName = "Navigator"

private val navigationFunctionImports = listOf(
    Pair("androidx.compose.runtime", "Composable"),
    Pair("androidx.navigation", "NavHostController"),
)

fun generateNavigatorFile(packageName: String, destinations: List<NavigationDestination>, generateLogging: Boolean = false) = with(FileSpec.builder(packageName, FileName)) {
    navigationFunctionImports.forEach { import ->
        addImport(packageName = import.first, import.second)
    }

    addType(generateNavigatorClass(destinations))
    build()
}

private fun generateNavigatorClass(destinations: List<NavigationDestination>) = with(TypeSpec.classBuilder("Navigator")) {
    primaryConstructor(generateConstructor())
    addProperty(
        PropertySpec.builder("navHostController", TypeNames.Classes.NavHostController)
            .initializer("navHostController")
            .addModifiers(KModifier.PRIVATE)
            .build()
    )

    destinations.forEach { destination ->
        addFunction(generateNavigationFunction(destination))
    }

    addFunction(generateNavigateHomeFunction(destinations))
    addFunction(navigateUpFunction)

    build()
}


private fun generateConstructor() = with(FunSpec.constructorBuilder()) {
    addParameter("navHostController", TypeNames.Classes.NavHostController)
    build()
}

private fun generateNavigationFunction(destination: NavigationDestination, specialName: String? = null): FunSpec {
    val functionParameters = destination.parameters.filterNavigator().map { parameter -> navigationFunctionParameters(parameter) }
    val actualName = if (destination.customName.isNullOrEmpty()) destination.actualName else destination.customName
    val name = specialName ?: "navigateTo$actualName"

    return with(FunSpec.builder(name)) {
        addNavigationFunctionBody(destination)
        addParameters(functionParameters)
        addParameter(navOptionsParameter)
        build()
    }
}


private val navOptionsBuilderLambda by lazy {
    LambdaTypeName.get(receiver = TypeNames.Classes.NavOptionsBuilder, returnType = UNIT)
}

private val navOptionsParameter by lazy {
    ParameterSpec
        .builder("navOptions", navOptionsBuilderLambda.copy(nullable = true))
        .defaultValue("null")
        .build()
}

private val navigateUpFunction by lazy {
    FunSpec
        .builder("navigateUp")
        .addStatement("navHostController.navigateUp()")
        .build()
}

private fun generateNavigateHomeFunction(destinations: List<NavigationDestination>) = generateNavigationFunction(destinations.single { it.isHome }, specialName = "navigateHome")

private fun navigationFunctionParameters(parameter: NavigationParameter) =
    ParameterSpec
        .builder(parameter.name, parameter.type.className.copy(nullable = parameter.isNullable))
        .build()

private fun FunSpec.Builder.addNavigationFunctionBody(destination: NavigationDestination) = with(this) {
    val navRouteActual = navRouteActual(destination)
    addStatement("navHostController.navigate(\"%L\", navOptions ?: { })", navRouteActual)
}

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

