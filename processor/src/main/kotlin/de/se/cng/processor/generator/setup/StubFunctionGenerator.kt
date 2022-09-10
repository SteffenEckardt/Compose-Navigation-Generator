package de.se.cng.processor.generator.setup

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import de.se.cng.processor.generator.TypeNames
import de.se.cng.processor.models.NavigationDestination

class StubFunctionGenerator(private val `package`: String, enableLogging: Boolean) :
    SetupFunctionGeneratorBase(enableLogging) {

    override fun generate() = with(FileSpec.builder(`package`, fileName)) {
        addImports()
        addSetupFunction()
        loggingTag()
        build()
    }

    override fun FileSpec.Builder.addSetupFunction() = addFunction(FunSpec
        .builder("SetupNavHost")
        .addAnnotation(TypeNames.Annotations.Composable)
        .addParameter(Parameter_NavHostController, TypeNames.Classes.NavHostController)
        .addSetupBody(emptySet())
        .build()
    )

    override fun FunSpec.Builder.addSetupBody(destinations: Set<NavigationDestination>) =
        addStatement("TODO(%S)", "Compose navigation could not be generated. Check build log for more details.")


    override fun FunSpec.Builder.addComposableCall(destination: NavigationDestination) = this

}