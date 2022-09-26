@file:Suppress("TestFunctionName")

package de.se.cng.processor.utils

import de.se.cng.processor.generator.ParameterType
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.models.NavigationParameter

@DslMarker
annotation class SourceFactoryDsl

@SourceFactoryDsl
fun SourceFactory(init: DestinationBuilder.() -> Unit): List<NavigationDestination> {
    val destinationBuilder = object : DestinationBuilder() {
        public override fun invoke(): List<NavigationDestination> {
            return super.invoke()
        }
    }
    destinationBuilder.init()
    return destinationBuilder()
}

open class ParameterBuilder {
    private val parameters = mutableSetOf<NavigationParameter>()

    protected open operator fun invoke(): Set<NavigationParameter> = parameters

    @SourceFactoryDsl
    fun Parameter(name: String, type: ParameterType, nullable: Boolean = false) {
        parameters += NavigationParameter(
            name,
            type,
            nullable
        )
    }

    @SourceFactoryDsl
    fun Navigator() = Parameter("navigator", ParameterType.Navigator, false)

    @SourceFactoryDsl
    fun Name(nullable: Boolean = false) = Parameter("name", ParameterType.String, nullable)

    @SourceFactoryDsl
    fun Age(nullable: Boolean = false) = Parameter("age", ParameterType.Int, nullable)

    @SourceFactoryDsl
    fun Height(nullable: Boolean = false) = Parameter("height", ParameterType.Float, nullable)

    @SourceFactoryDsl
    fun Weight(nullable: Boolean = false) = Parameter("weight", ParameterType.Float, nullable)

}

open class DestinationBuilder {
    private val destinations = mutableListOf<NavigationDestination>()

    protected open operator fun invoke(): List<NavigationDestination> = destinations

    private fun Destination(name: String, isHome: Boolean, initParameters: ParameterBuilder.() -> Unit) {
        val parameters = parameterBuilder()
        parameters.initParameters()
        val parameterList = parameters.invoke()

        destinations += NavigationDestination(
            actualName = name,
            actualPackage = "de.se.ui",
            isHome = isHome,
            parameters = parameterList
        )
    }

    @SourceFactoryDsl
    fun Home(initParameters: ParameterBuilder.() -> Unit) = Destination("HomeDestination", true, initParameters)

    @SourceFactoryDsl
    fun Details(initParameters: ParameterBuilder.() -> Unit) = Destination("DetailDestination", false, initParameters)

    @SourceFactoryDsl
    fun List(initParameters: ParameterBuilder.() -> Unit) = Destination("ListDestination", false, initParameters)

    private fun parameterBuilder() = object : ParameterBuilder() {
        public override fun invoke(): Set<NavigationParameter> {
            return super.invoke()
        }
    }
}

