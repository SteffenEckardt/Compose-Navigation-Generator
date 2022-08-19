package de.se.cng.processor.models

data class NavigationParameter(
    val name: String,
    val typeName: String,
    val typePackage: String,
    val isNullable: Boolean = false,
)