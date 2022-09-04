package de.se.cng.processor.generator

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec

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
)

fun generateStubSetupFile(`package`: String, generateLogging: Boolean = false) = with(FileSpec.builder(`package`, FileName)) {
    navHostImports.forEach { import ->
        addImport(packageName = import.first, import.second)
    }

    addFunction(generateStubSetupFunction())
    if (generateLogging) addProperty(loggingTag(FileName))

    build()
}

private fun generateStubSetupFunction(): FunSpec = with(FunSpec.builder("SetupNavHost")) {
    addAnnotation(TypeNames.Annotations.Composable)
    addParameter(Parameter_NavHostController, TypeNames.Classes.NavHostController)
    addStatement("TODO(%S)", "Compose navigation could not be generated. Check build log for more details.")
}.build()
