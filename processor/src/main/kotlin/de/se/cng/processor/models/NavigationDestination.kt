package de.se.cng.processor.models

data class NavigationDestination(
    val actualName: String,
    val actualPackage: String,
    val customName: String? = null,
    val isHome: Boolean = false,
    val parameters: List<NavigationParameter> = emptyList(),
)