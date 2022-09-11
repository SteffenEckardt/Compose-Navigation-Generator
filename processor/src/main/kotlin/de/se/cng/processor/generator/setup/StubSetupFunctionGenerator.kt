package de.se.cng.processor.generator.setup

import com.squareup.kotlinpoet.FunSpec
import de.se.cng.processor.models.NavigationDestination

class StubSetupFunctionGenerator(`package`: String, enableLogging: Boolean) :
    SetupFunctionGeneratorBase(`package`, emptySet(), enableLogging) {

    override fun FunSpec.Builder.addSetupBody(destinations: Set<NavigationDestination>) =
        addStatement("TODO(%S)", "Compose navigation could not be generated. Check build log for more details.")

    override fun FunSpec.Builder.addComposableCall(destination: NavigationDestination) = this

}