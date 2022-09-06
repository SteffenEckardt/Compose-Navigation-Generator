package de.se.cng.processor.generator

import com.squareup.kotlinpoet.ClassName
import de.se.cng.processor.exceptions.UnsupportedParameterTypeException

internal object TypeNames {
    object Classes {
        val NavHostController = ClassName("androidx.navigation", "NavHostController")
        val Navigator = ClassName("de.se.cng.generated", "Navigator")
        val NavOptionsBuilder = ClassName("androidx.navigation", "NavOptionsBuilder")
    }

    object Annotations {
        val Composable = ClassName("androidx.compose.runtime", "Composable")
    }
}

sealed class ParameterType(packageName: kotlin.String, simpleName: kotlin.String) {
    val className = ClassName(packageName, simpleName)

    object String : ParameterType("kotlin", "String")
    object Int : ParameterType("kotlin", "Int")
    object Double : ParameterType("kotlin", "Double")
    object Float : ParameterType("kotlin", "Float")
    object Boolean : ParameterType("kotlin", "Boolean")
    object Long : ParameterType("kotlin", "Long")
    object Byte : ParameterType("kotlin", "Byte")
    object Short : ParameterType("kotlin", "Short")
    object Char : ParameterType("kotlin", "Char")

    object NavHostController : ParameterType("androidx.navigation", "NavHostController")
    object Navigator : ParameterType("de.se.cng.generated", "Navigator")

    companion object {
        fun from(className: ClassName): ParameterType = from(className.canonicalName)

        fun from(className: kotlin.String): ParameterType = when (className) {
            "kotlin.String"                         -> String
            "kotlin.Int"                            -> Int
            "kotlin.Double"                         -> Double
            "kotlin.Float"                          -> Float
            "kotlin.Boolean"                        -> Boolean
            "kotlin.Long"                           -> Long
            "kotlin.Byte"                           -> Byte
            "kotlin.Short"                          -> Short
            "kotlin.Char"                           -> Char
            "androidx.navigation.NavHostController" -> NavHostController
            "Navigator"         -> Navigator
            else                                    -> throw UnsupportedParameterTypeException(className)
        }
    }
}