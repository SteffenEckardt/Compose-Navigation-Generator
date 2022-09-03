package de.se.cng.processor.exceptions

import com.squareup.kotlinpoet.ClassName
import de.se.cng.processor.generator.ParameterType

class UnsupportedParameterTypeException(val className: ClassName) : Throwable(className.canonicalName) {
    constructor(parameterType: ParameterType) : this(parameterType.className)
}