package de.se.cng.processor.generator.navigator

import com.squareup.kotlinpoet.*
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.models.NavigationParameter


class StubNavigatorClassGenerator(
    packageName: String,
    destinationNames: Set<String>,
    generateLogging: Boolean = false,
) : NavigatorClassGeneratorBase(packageName, destinationNames.map { NavigationDestination(it, "") }.toSet(), generateLogging) {

    override fun TypeSpec.Builder.addNavigationFunction(destination: NavigationDestination, specialName: String?) =
        addFunction(FunSpec.builder("navigateTo${destination.actualName}")
            .addNavigationFunctionParameters(emptySet())
            .addStatement("TODO(%S)", "Compose navigation could not be generated. Check build log for more details.")
            .build()
        )

    override fun TypeSpec.Builder.addNavigateHomeFunction(destinations: Set<NavigationDestination>) =
        addFunction(FunSpec.builder("navigateHome")
            .addNavigationFunctionParameters(emptySet())
            .addStatement("TODO(%S)", "Compose navigation could not be generated. Check build log for more details.")
            .build()
        )

    override fun FunSpec.Builder.addNavigationFunctionParameters(parameters: Set<NavigationParameter>) = addParameter(
        ParameterSpec.builder("stub", ANY.copy(true), KModifier.VARARG).build()
    )

}

