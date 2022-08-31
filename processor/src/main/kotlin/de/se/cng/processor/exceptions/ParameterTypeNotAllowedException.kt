package de.se.cng.processor.exceptions

import com.squareup.kotlinpoet.ClassName

class ParameterTypeNotAllowedException(className: ClassName): Throwable(className.canonicalName)