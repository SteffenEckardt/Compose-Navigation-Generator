package de.se.cng.processor.generator

import com.squareup.kotlinpoet.ClassName
import de.se.cng.processor.exceptions.ParameterTypeNotAllowedException

internal object TypeNames {
    object Classes {
        val NavHostController = ClassName("androidx.navigation", "NavHostController")
    }

    object Functions {
        val NavHost = ClassName("androidx.navigation", "NavHostController")
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

    class List(val contentType: ParameterType) : ParameterType("kotlin.collections", "List")
    class Set(val contentType: ParameterType) : ParameterType("kotlin.collections", "Set")
    class Map(val keyType: ParameterType, val valueType: ParameterType) : ParameterType("kotlin.collections", "Map")

    object NavHostController : ParameterType("androidx.navigation", "NavHostController")

    companion object {
        fun from(className: ClassName): ParameterType = when (className.canonicalName) {
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
            else                                    -> throw ParameterTypeNotAllowedException(className)
        }

        fun from(className: ClassName, contentType: ClassName): ParameterType = when (className.canonicalName) {
            "kotlin.collections.List" -> List(from(contentType))
            "kotlin.collections.Set"  -> Set(from(contentType))
            else                      -> throw ParameterTypeNotAllowedException(className)
        }

        fun from(className: ClassName, keyType: ClassName, valueType: ClassName): ParameterType = when (className.canonicalName) {
            "kotlin.collections.Map" -> Map(from(keyType), from(valueType))
            else                     -> throw ParameterTypeNotAllowedException(className)
        }
    }
}