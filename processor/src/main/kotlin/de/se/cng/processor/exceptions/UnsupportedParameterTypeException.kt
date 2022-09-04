package de.se.cng.processor.exceptions

import com.squareup.kotlinpoet.ClassName
import de.se.cng.processor.generator.ParameterType

class UnsupportedParameterTypeException(val className: String) : Throwable(className) {
    constructor(parameterType: ParameterType) : this(parameterType.className)
    constructor(className: ClassName) : this(className.canonicalName)
}