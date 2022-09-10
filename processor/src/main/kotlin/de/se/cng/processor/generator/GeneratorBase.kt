package de.se.cng.processor.generator

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec

abstract class GeneratorBase(protected val fileName: String, private val enableLogging: Boolean) {
    protected abstract fun FileSpec.Builder.addImports(): FileSpec.Builder

    abstract fun generate(): FileSpec

    protected fun FileSpec.Builder.loggingTag() = if (enableLogging) {
        addProperty(PropertySpec
            .builder("TAG", String::class, KModifier.PRIVATE, KModifier.CONST)
            .initializer("%S", fileName)
            .build())
    } else this

    protected fun FunSpec.Builder.logd(message: String) = if (enableLogging) addStatement("Log.d(TAG, \"%L\")", message) else this
    protected fun FunSpec.Builder.logi(message: String) = if (enableLogging) addStatement("Log.i(TAG, \"%L\")", message) else this
    protected fun FunSpec.Builder.logw(message: String) = if (enableLogging) addStatement("Log.i(TAG, \"%L\")", message) else this
    protected fun FunSpec.Builder.loge(message: String) = if (enableLogging) addStatement("Log.e(TAG, \"%L\")", message) else this
}