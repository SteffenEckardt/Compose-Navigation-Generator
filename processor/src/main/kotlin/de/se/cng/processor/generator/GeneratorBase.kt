package de.se.cng.processor.generator

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec

abstract class GeneratorBase(protected val fileName: String, private val enableLogging: Boolean) {
    protected abstract fun FileSpec.Builder.addImports(): FileSpec.Builder

    abstract fun generate(): FileSpec

    protected fun FunSpec.Builder.logd(message: String) = if (enableLogging) addStatement("Log.d(%S, \"%L\")", fileName, message) else this
    protected fun FunSpec.Builder.logi(message: String) = if (enableLogging) addStatement("Log.i(%S, \"%L\")", fileName, message) else this
    protected fun FunSpec.Builder.logw(message: String) = if (enableLogging) addStatement("Log.i(%S, \"%L\")", fileName, message) else this
    protected fun FunSpec.Builder.loge(message: String) = if (enableLogging) addStatement("Log.e(%S, \"%L\")", fileName, message) else this
}