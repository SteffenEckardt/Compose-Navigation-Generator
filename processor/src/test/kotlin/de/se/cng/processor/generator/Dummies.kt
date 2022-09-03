package de.se.cng.processor.generator

import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.models.NavigationParameter

internal object Dummies {
    object Parameters {
        fun navigator() = NavigationParameter(
            "name",
            ParameterType.Navigator,
            false
        )

        fun name(nullable: Boolean = false) = NavigationParameter(
            "name",
            ParameterType.String,
            nullable
        )

        fun age(nullable: Boolean = false) = NavigationParameter(
            "age",
            ParameterType.Int,
            nullable
        )

        fun height(nullable: Boolean = false) = NavigationParameter(
            "height",
            ParameterType.Float,
            nullable
        )

        fun weight(nullable: Boolean = false) = NavigationParameter(
            "weight",
            ParameterType.Float,
            nullable
        )
    }

    object Destinations {
        fun home(vararg parameters: NavigationParameter) = NavigationDestination(
            actualName = "HomeDestination",
            actualPackage = "de.se.ui",
            isHome = true,
            parameters = buildSet {
                add(Parameters.navigator())
                addAll(parameters)
            }
        )

        fun details(vararg parameters: NavigationParameter) = NavigationDestination(
            actualName = "DetailDestination",
            actualPackage = "de.se.ui",
            parameters = buildSet {
                add(Parameters.navigator())
                addAll(parameters)
            }
        )

        fun list(vararg parameters: NavigationParameter) = NavigationDestination(
            actualName = "ListDestination",
            actualPackage = "de.se.ui",
            parameters = buildSet {
                add(Parameters.navigator())
                addAll(parameters)
            }
        )

        fun info(vararg parameters: NavigationParameter) = NavigationDestination(
            actualName = "InfoDestination",
            actualPackage = "de.se.ui",
            parameters = buildSet {
                add(Parameters.navigator())
                addAll(parameters)
            }
        )
    }
}
