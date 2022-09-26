package de.se.cng.processor.generator.setup

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import de.se.cng.processor.generator.GeneratorBase
import de.se.cng.processor.generator.TypeNames
import de.se.cng.processor.models.NavigationDestination

abstract class SetupFunctionGeneratorBase(
    private val `package`: String,
    private val destinations: Set<NavigationDestination>,
    enableLogging: Boolean,
) : GeneratorBase("NavHost", enableLogging) {

    protected val Parameter_NavHostController = "navHostController"

    override fun FileSpec.Builder.addImports() = this
        .addImport("android.util", "Log")
        .addImport("androidx.compose.runtime", "Composable")
        .addImport("androidx.navigation", "NavHostController")
        .addImport("androidx.navigation", "navArgument")
        .addImport("androidx.navigation", "NavType")
        .addImport("androidx.navigation.compose", "NavHost")
        .addImport("androidx.navigation.compose", "composable")

    override fun generate() = with(FileSpec.builder(`package`, fileName)) {
        addImports()
        addSetupFunction()
        build()
    }

    fun FileSpec.Builder.addSetupFunction() = addFunction(FunSpec
        .builder("SetupNavHost")
        .addAnnotation(TypeNames.Annotations.Composable)
        .addParameter(Parameter_NavHostController, TypeNames.Classes.NavHostController)
        .addSetupBody(destinations)
        .build()
    )

    abstract fun FunSpec.Builder.addSetupBody(destinations: Set<NavigationDestination>): FunSpec.Builder
    abstract fun FunSpec.Builder.addComposableCall(destination: NavigationDestination): FunSpec.Builder
}