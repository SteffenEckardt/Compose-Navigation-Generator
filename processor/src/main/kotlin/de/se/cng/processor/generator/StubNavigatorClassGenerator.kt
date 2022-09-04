package de.se.cng.processor.generator

import com.squareup.kotlinpoet.*

private const val FileName = "Navigator"

private val navigationFunctionImports = listOf(
    Pair("androidx.compose.runtime", "Composable"),
    Pair("androidx.navigation", "NavHostController"),
)

fun generateStubNavigatorFile(packageName: String, destinationNames: Set<String>, generateLogging: Boolean = false) = with(FileSpec.builder(packageName, FileName)) {
    navigationFunctionImports.forEach { import ->
        addImport(packageName = import.first, import.second)
    }

    addType(generateStubNavigatorClass(destinationNames))
    build()
}

private fun generateStubNavigatorClass(destinations: Set<String>) = with(TypeSpec.classBuilder("Navigator")) {
    primaryConstructor(generateConstructor())
    addProperty(
        PropertySpec.builder("navHostController", TypeNames.Classes.NavHostController)
            .initializer("navHostController")
            .addModifiers(KModifier.PRIVATE)
            .build()
    )

    destinations.forEach { destination ->
        addFunction(generateStubNavigationFunction(destination))
    }
    build()
}

private fun generateConstructor() = with(FunSpec.constructorBuilder()) {
    addParameter("navHostController", TypeNames.Classes.NavHostController)
    build()
}

private fun generateStubNavigationFunction(destinationName: String): FunSpec {
    val wildcardParameter = ParameterSpec.builder("stub", ANY.copy(true), KModifier.VARARG).build()

    return with(FunSpec.builder("navigateTo$destinationName")) {
        addParameter(wildcardParameter)
        addStatement("TODO(%S)", "Compose navigation could not be generated. Check build log for more details.")
        build()
    }
}
