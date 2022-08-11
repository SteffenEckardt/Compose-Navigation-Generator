package composektx.navigationgenerator.processor.extensions

import org.intellij.lang.annotations.Language

fun kotlinCode(@Language("Kotlin") code: String) = code

fun <T> T?.requireNotNull() = this.apply { requireNotNull(this) }!!

@Suppress("SpellCheckingInspection")
fun String.pascalcase(): String = this.replaceFirstChar { this.first().uppercase() }