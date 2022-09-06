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
    fun `ignores unmarked functions`() {

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
        import androidx.navigation.NavOptionsBuilder
        import kotlin.Unit
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(navOptions: (NavOptionsBuilder.() -> Unit)? = null): Unit {
            navHostController.navigate("HomeDestination", navOptions ?: { })
          }
          public fun navigateHome(navOptions: (NavOptionsBuilder.() -> Unit)? = null): Unit {
            navHostController.navigate("HomeDestination", navOptions ?: { })
          }
        
          public fun navigateUp(): Unit {
            navHostController.navigateUp()
          }
        }
        """.trimIndent()

        assertSourceEquals(expectedSetupNavHost, compilationResult.sourceFor("NavHost.kt"))
        assertSourceEquals(expectedNavigator, compilationResult.sourceFor("Navigator.kt"))

    }

    @Test
    fun `incremental processing`() {

        val kotlinSource1 = SourceFile.kotlin("FirstScreen.kt",
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

        val kotlinSource21 = SourceFile.kotlin("FirstScreen.kt",
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

        val kotlinSource22 = SourceFile.kotlin("SecondScreen.kt",
            """
            package app.ui.screens.first
            
            import androidx.compose.runtime.Composable
            import de.se.cng.annotation.*
            
            @Destination
            @Composable
            fun DetailDestination() {
            
            }
            """.trimIndent()
        )

        val expectedSetupNavHost1 = """
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

        val expectedSetupNavHost2 = """
        package $PACKAGE
        
        import android.util.Log
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import androidx.navigation.NavType
        import androidx.navigation.compose.NavHost
        import androidx.navigation.compose.composable
        import androidx.navigation.navArgument
        import app.ui.screens.first.HomeDestination
        import app.ui.screens.first.DetailDestination
        import kotlin.Unit
        
        @Composable
        public fun SetupNavHost(navController: NavHostController): Unit {
          NavHost(navController = navController, startDestination = "HomeDestination")
          {
            composable("HomeDestination") { 
              HomeDestination()
            }
            composable("DetailDestination") { 
              DetailDestination()
            }
          }
        }
        """.trimIndent()

        val expectedNavigator1 = """
        package $PACKAGE
        
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import androidx.navigation.NavOptionsBuilder
        import kotlin.Unit
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(navOptions: (NavOptionsBuilder.() -> Unit)? = null): Unit {
            navHostController.navigate("HomeDestination", navOptions ?: { })
          }
          public fun navigateHome(navOptions: (NavOptionsBuilder.() -> Unit)? = null): Unit {
            navHostController.navigate("HomeDestination", navOptions ?: { })
          }
          public fun navigateUp(): Unit {
            navHostController.navigateUp()
          }
        }
        """.trimIndent()

        val expectedNavigator2 = """
        package $PACKAGE
        
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import androidx.navigation.NavOptionsBuilder
        import kotlin.Unit
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(navOptions: (NavOptionsBuilder.() -> Unit)? = null): Unit {
            navHostController.navigate("HomeDestination", navOptions ?: { })
          }
          public fun navigateToDetailDestination(navOptions: (NavOptionsBuilder.() -> Unit)? = null): Unit {
            navHostController.navigate("DetailDestination", navOptions ?: { })
          }
          public fun navigateHome(navOptions: (NavOptionsBuilder.() -> Unit)? = null): Unit {
            navHostController.navigate("HomeDestination", navOptions ?: { })
          }
          public fun navigateUp(): Unit {
            navHostController.navigateUp()
          }
        }
        """.trimIndent()

        val compilationResult1 = compile(kotlinSource1)
        assertEquals(KotlinCompilation.ExitCode.OK, compilationResult1.exitCode)
        assertSourceEquals(expectedSetupNavHost1, compilationResult1.sourceFor("NavHost.kt"))
        assertSourceEquals(expectedNavigator1, compilationResult1.sourceFor("Navigator.kt"))

        val compilationResult2 = compile(kotlinSource21, kotlinSource22)
        assertEquals(KotlinCompilation.ExitCode.OK, compilationResult2.exitCode)
        assertSourceEquals(expectedSetupNavHost2, compilationResult2.sourceFor("NavHost.kt"))
        assertSourceEquals(expectedNavigator2, compilationResult2.sourceFor("Navigator.kt"))

    }

}