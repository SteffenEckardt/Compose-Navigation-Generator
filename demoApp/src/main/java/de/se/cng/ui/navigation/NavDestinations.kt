package de.se.cng.ui.navigation

import de.se.cng.extensions.deepEquals

internal sealed class Route(protected val base: String) {
    abstract fun asString(): String

    class SimpleRoute(base: String) : Route(base) {
        override fun asString() = base
        fun asNavigation() = base
    }

    class NonOptionalRoute(base: String, private vararg val parameterKeys: String) : Route(base) {
        override fun asString() = "$base${parameterKeys.joinToString(separator = "") { key -> "/${key}" }}"

        fun asNavigation(vararg actualValues: Pair<String, Any>) = asNavigation(actualValues.toMap())

        private fun asNavigation(actualValues: Map<String, Any>): String {
            require(parameterKeys.toList().deepEquals(actualValues.keys))
            return base + parameterKeys.joinToString(separator = "/") { key ->
                actualValues[key].toString()
            }
        }
    }

    class OptionalRoute(base: String, private vararg val parameterKeys: String) : Route(base) {
        override fun asString() = "$base${parameterKeys.joinToString(separator = "") { key -> "&$key={${key}}" }.replaceFirst('&', '?')}"

        fun asNavigation(vararg actualValues: Pair<String, Any?>) = asNavigation(actualValues.toMap())

        private fun asNavigation(actualValues: Map<String, Any?>): String {
            require(parameterKeys.toList().containsAll(actualValues.keys))

            return buildString {
                append("$base?")
                append(parameterKeys.joinToString(separator = "&") { key ->
                    "$key=${actualValues[key].toString()}"
                })
            }
        }
    }
}


sealed class NavigationDestination(internal val route: Route) {
    object HomeDestination : NavigationDestination(Route.SimpleRoute("home"))
    object ListDestination : NavigationDestination(Route.SimpleRoute("list"))
    object DetailDestination : NavigationDestination(Route.OptionalRoute("detail", "param1", "param2", "param3", "param4"))
}
