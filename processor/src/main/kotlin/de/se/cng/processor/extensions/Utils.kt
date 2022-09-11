package de.se.cng.processor.extensions

import org.intellij.lang.annotations.Language

fun <T> T?.requireNotNull() = this.apply { requireNotNull(this) }!!

@Suppress("SpellCheckingInspection")
fun String.pascalcase(): String = this.replaceFirstChar { this.first().uppercase() }