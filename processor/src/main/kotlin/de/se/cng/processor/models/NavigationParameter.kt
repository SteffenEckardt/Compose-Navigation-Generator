package de.se.cng.processor.models

import com.squareup.kotlinpoet.ClassName

data class NavigationParameter(
    val name: String,
    val className: ClassName,
    val isNullable: Boolean = false,
)