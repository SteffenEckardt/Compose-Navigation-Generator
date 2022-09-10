package de.se.cng.processor.generator.navigator

import com.squareup.kotlinpoet.*
import de.se.cng.processor.generator.GeneratorBase
import de.se.cng.processor.generator.TypeNames
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.models.NavigationParameter

abstract class NavigatorClassGeneratorBase(
    private val `package`: String,
    private val destinations: Set<NavigationDestination>,
    enableLogging: Boolean,
) : GeneratorBase("Navigator", enableLogging) {

    override fun generate() = with(FileSpec.builder(`package`, fileName)) {
        addImports()
        addNavigatorClass(destinations)
        build()
    }

    override fun FileSpec.Builder.addImports() = this
        .addImport(TypeNames.Classes.NavHostController.packageName, TypeNames.Classes.NavHostController.simpleName)
        .addImport(TypeNames.Annotations.Composable.packageName, TypeNames.Annotations.Composable.simpleName)


    protected fun TypeSpec.Builder.addConstructor() = primaryConstructor(FunSpec
        .constructorBuilder()
        .addParameter("navHostController", TypeNames.Classes.NavHostController)
        .build()
    )

    protected fun TypeSpec.Builder.addNavHostControllerProperty() = addProperty(PropertySpec
        .builder("navHostController", TypeNames.Classes.NavHostController)
        .initializer("navHostController")
        .addModifiers(KModifier.PRIVATE)
        .build()
    )

    protected fun TypeSpec.Builder.navigateUpFunction() = addFunction(FunSpec
        .builder("navigateUp")
        .addStatement("navHostController.navigateUp()")
        .build()
    )

    protected abstract fun FileSpec.Builder.addNavigatorClass(destinations: Set<NavigationDestination>): FileSpec.Builder
    protected abstract fun TypeSpec.Builder.addNavigationFunction(destination: NavigationDestination, specialName: String? = null): TypeSpec.Builder
    protected abstract fun TypeSpec.Builder.addNavigateHomeFunction(destinations: Set<NavigationDestination>): TypeSpec.Builder
    protected abstract fun FunSpec.Builder.addNavigationFunctionParameters(parameters: Set<NavigationParameter>): FunSpec.Builder

}
