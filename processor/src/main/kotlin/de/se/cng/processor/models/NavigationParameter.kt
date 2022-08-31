package de.se.cng.processor.models

import de.se.cng.processor.generator.ParameterType

data class NavigationParameter(
    val name: String,
    val type: ParameterType,
    val isNullable: Boolean = false,
)