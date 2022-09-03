/*
    Why are these warnings ignored?
    ======================================================================================
    "RedundantVisibilityModifier"   -> KotlinPoet generates explicit public functions.
    "RedundantUnitReturnType"       -> KotlinPoet generates explicit Unit return type.
    "TestFunctionName"              -> Compose functions start with an uppercase Letter.
    "unused"                        -> Navigation functions are not called in test context.
*/
@file:Suppress("RedundantVisibilityModifier", "TestFunctionName", "RedundantUnitReturnType", "unused")

package de.se.cng.processor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import de.se.cng.processor.generator.NavigatorClassGeneratorTest
import de.se.cng.processor.processor.DestinationAnnotationProcessor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Destination Annotation Processor")
class DestinationProcessorTest : ProcessorTestBase() {


    companion object {
        private const val PACKAGE = DestinationAnnotationProcessor.PACKAGE
    }

    @Test
    fun `ignores non marked functions`() {

        val kotlinSource = SourceFile.kotlin("FirstScreen.kt",
            """
            package app.ui.screens.first
                    
            import androidx.compose.runtime.Composable
            import de.se.cng.annotation.Destination
            
            @Composable
            fun FirstDestination() {
            
            }
            """
        )

        val compilationResult = compile(kotlinSource)
        assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        assertEmpty(compilationResult.generatedFiles)

    }

    @Test
    fun `single destination, no arguments`() {

        val kotlinSource = SourceFile.kotlin("FirstScreen.kt",
            """
            package app.ui.screens.first
            
            import androidx.compose.runtime.Composable
            import de.se.cng.annotation.*
            
            @Home
            @Destination
            @Composable
            fun HomeDestination() {
            
            }
            """.trimIndent()
        )

        val compilationResult = compile(kotlinSource)
        assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

        val expectedSetupNavHost = """
        package $PACKAGE
        
        import android.util.Log
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import androidx.navigation.NavType
        import androidx.navigation.compose.NavHost
        import androidx.navigation.compose.composable
        import androidx.navigation.navArgument
        import app.ui.screens.first.HomeDestination
        import kotlin.Unit
        
        @Composable
        public fun SetupNavHost(navController: NavHostController): Unit {
          NavHost(navController = navController, startDestination = "HomeDestination")
          {
            composable("HomeDestination") { 
              HomeDestination()
            }
          }
        }
        """.trimIndent()

        val expectedNavigator = """
        package $PACKAGE
        
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import kotlin.Unit
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(): Unit {
            navHostController.navigate("HomeDestination")
          }
        }
        """.trimIndent()

        assertSourceEquals(expectedSetupNavHost, compilationResult.sourceFor("NavHost.kt"))
        assertSourceEquals(expectedNavigator, compilationResult.sourceFor("Navigator.kt"))

    }

}