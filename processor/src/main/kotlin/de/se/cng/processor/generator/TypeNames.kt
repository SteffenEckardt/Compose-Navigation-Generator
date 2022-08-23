package de.se.cng.processor.generator

import com.squareup.kotlinpoet.ClassName

internal object TypeNames {
    object Classes {
        val NavHostController = ClassName("androidx.navigation", "NavHostController")
    }
    object Functions {
        val NavHost = ClassName("androidx.navigation", "NavHostController")
    }
}