package de.se.cng.processor.generator

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec

fun loggingTag(tag: String) = PropertySpec
    .builder("TAG", String::class, KModifier.PRIVATE, KModifier.CONST)
    .initializer("%S", tag)
    .build()
