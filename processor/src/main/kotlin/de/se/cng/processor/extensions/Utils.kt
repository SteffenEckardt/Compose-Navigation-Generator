package de.se.cng.processor.extensions

fun <T> T?.requireNotNull() = this.apply { requireNotNull(this) }!!

@Suppress("SpellCheckingInspection")
fun String.pascalcase(): String = this.replaceFirstChar { this.first().uppercase() }