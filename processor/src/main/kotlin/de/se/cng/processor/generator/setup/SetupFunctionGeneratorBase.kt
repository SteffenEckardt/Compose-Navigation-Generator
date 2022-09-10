package de.se.cng.processor.generator.setup

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import de.se.cng.processor.generator.GeneratorBase
import de.se.cng.processor.models.NavigationDestination

abstract class SetupFunctionGeneratorBase(enableLogging: Boolean) : GeneratorBase("NavHost", enableLogging) {

    protected val Parameter_NavHostController = "navHostController"

    override fun FileSpec.Builder.addImports() = this
        .addImport("android.util", "Log")
        .addImport("androidx.compose.runtime", "Composable")
        .addImport("androidx.navigation", "NavHostController")
        .addImport("androidx.navigation", "navArgument")
        .addImport("androidx.navigation", "NavType")
        .addImport("androidx.navigation.compose", "NavHost")
        .addImport("androidx.navigation.compose", "composable")

    abstract override fun generate(): FileSpec
    abstract fun FileSpec.Builder.addSetupFunction(): FileSpec.Builder
    abstract fun FunSpec.Builder.addSetupBody(destinations: Set<NavigationDestination>): FunSpec.Builder
    abstract fun FunSpec.Builder.addComposableCall(destination: NavigationDestination): FunSpec.Builder
}