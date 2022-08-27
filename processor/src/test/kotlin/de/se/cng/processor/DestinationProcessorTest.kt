package de.se.cng.processor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Destination Annotation Processor")
class DestinationProcessorTest : ProcessorTestBase() {

    @Test
    fun `ignores non marked functions`() {

        val kotlinSource = SourceFile.kotlin("FirstScreen.kt",
            """package mypackage.ui.screens.first
        
                import androidx.compose.runtime.Composable
                import de.se.cng.annotation.Destination
    
                @Composable
                fun FirstDestination() {
                
                }"""
        )

        val compilationResult = compile(kotlinSource)
        assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        assertEmpty(compilationResult.generatedFiles)

    }

    @DisplayName("NavHost Generator")
    @Nested
    inner class NavHostGenerator {


        @Test
        fun `setup for 1 destination with no arguments`() {

            val kotlinSource = SourceFile.kotlin("Composables.kt",
                """
                package de.se.cng                
                
                import androidx.compose.runtime.Composable
                import de.se.cng.annotation.Destination
                import de.se.cng.annotation.Home
                
                @Composable
                @Home
                @Destination
                fun HomeDestination() {
                
                }
                """.trimIndent()
            )

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng
            
            import android.util.Log
            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import androidx.navigation.NavType
            import androidx.navigation.compose.NavHost
            import androidx.navigation.compose.composable
            import androidx.navigation.navArgument
            import kotlin.String
            import kotlin.Unit
            
            @Composable
            public fun SetupNavHost(navController: NavHostController): Unit {
              NavHost(navController = navController, startDestination = "HomeDestination")
              {
                composable("HomeDestination") {
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination()
                }
              }
            }
            
            private const val TAG: String = "NavHost"
            """.trimIndent()

            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavHost.kt")
            )
        }

        @Test
        fun `setup for 1 destination with 1 non-null argument`() {

            val kotlinSource = SourceFile.kotlin("Composables.kt",
                """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Home
                @Destination
                fun HomeDestination(name: String) {
                
                }
                """.trimIndent()
            )

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng
            
            import android.util.Log
            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import androidx.navigation.NavType
            import androidx.navigation.compose.NavHost
            import androidx.navigation.compose.composable
            import androidx.navigation.navArgument
            import kotlin.String
            import kotlin.Unit
            
            @Composable
            public fun SetupNavHost(navController: NavHostController): Unit {
              NavHost(navController = navController, startDestination = "HomeDestination/argName")
              {
                composable("HomeDestination/argName", arguments = listOf(
                  navArgument("argName"){
                    nullable = false
                    type = NavType.fromArgType("String","kotlin")
                  },
                )) { backStackEntry ->
                  val argName = backStackEntry.arguments?.getString("argName")
                  requireNotNull(argName)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(name=argName)
                }
              }
            }
            
            private const val TAG: String = "NavHost"
            """.trimIndent()

            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavHost.kt")
            )
        }

        @Test
        fun `setup for 1 destination with 1 nullable argument`() {

            val kotlinSource = SourceFile.kotlin("Composables.kt",
                """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Home
                @Destination
                fun HomeDestination(name: String?) {
                
                }
                """.trimIndent()
            )

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng
            
            import android.util.Log
            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import androidx.navigation.NavType
            import androidx.navigation.compose.NavHost
            import androidx.navigation.compose.composable
            import androidx.navigation.navArgument
            import kotlin.String
            import kotlin.Unit
            
            @Composable
            public fun SetupNavHost(navController: NavHostController): Unit {
              NavHost(navController = navController, startDestination = "HomeDestination?argName={name}")
              {
                composable("HomeDestination?argName={name}", arguments = listOf(
                  navArgument("argName"){
                    nullable = true
                    type = NavType.fromArgType("String","kotlin")
                  },
                )) { backStackEntry ->
                  val argName = backStackEntry.arguments?.getString("argName")
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(name=argName)
                }
              }
            }
            
            private const val TAG: String = "NavHost"
            """.trimIndent()

            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavHost.kt")
            )
        }

        @Test
        fun `setup for 2 destination with no arguments`() {

            val kotlinSource = listOf(
                SourceFile.kotlin("Composables1.kt",
                    """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Home
                @Destination
                fun HomeDestination() {
                
                }
                """.trimIndent()
                ),
                SourceFile.kotlin("Composables2.kt",
                    """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Destination
                fun DetailDestination() {
                
                }
                """.trimIndent()
                ),
            ).toTypedArray()

            val compilationResult = compile(*kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng
            
            import android.util.Log
            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import androidx.navigation.NavType
            import androidx.navigation.compose.NavHost
            import androidx.navigation.compose.composable
            import androidx.navigation.navArgument
            import kotlin.String
            import kotlin.Unit
            
            @Composable
            public fun SetupNavHost(navController: NavHostController): Unit {
              NavHost(navController = navController, startDestination = "HomeDestination")
              {
                composable("HomeDestination") {
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination()
                }
                composable("DetailDestination") {
                  Log.d(TAG, "Navigating to DetailDestination")
                  DetailDestination()
                }
              }
            }
            
            private const val TAG: String = "NavHost"
            """.trimIndent()

            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavHost.kt")
            )
        }

        @Test
        fun `setup for 2 destination with different arguments`() {

            val kotlinSource = listOf(
                SourceFile.kotlin("Composables1.kt",
                    """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Home
                @Destination
                fun HomeDestination(name: String) {
                
                }
                """.trimIndent()
                ),
                SourceFile.kotlin("Composables2.kt",
                    """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Destination
                fun DetailDestination(age: Int) {
                
                }
                """.trimIndent()
                ),
            ).toTypedArray()

            val compilationResult = compile(*kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng
            
            import android.util.Log
            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import androidx.navigation.NavType
            import androidx.navigation.compose.NavHost
            import androidx.navigation.compose.composable
            import androidx.navigation.navArgument
            import kotlin.String
            import kotlin.Unit
            
            @Composable
            public fun SetupNavHost(navController: NavHostController): Unit {
              NavHost(navController = navController, startDestination = "HomeDestination/argName")
              {
                composable("HomeDestination/argName", arguments = listOf(
                  navArgument("argName"){
                    nullable = false
                    type = NavType.fromArgType("String","kotlin")
                  },
                )) { backStackEntry ->
                  val argName = backStackEntry.arguments?.getString("argName")
                  requireNotNull(argName)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(name=argName)
                }
                composable("DetailDestination/argAge", arguments = listOf(
                  navArgument("argAge"){
                    nullable = false
                    type = NavType.fromArgType("Int","kotlin")
                  },
                )) { backStackEntry ->
                  val argAge = backStackEntry.arguments?.getInt("argAge")
                  requireNotNull(argAge)
                  Log.d(TAG, "Navigating to DetailDestination")
                  DetailDestination(age=argAge)
                }
              }
            }
            
            private const val TAG: String = "NavHost"
            """.trimIndent()

            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavHost.kt")
            )
        }


        @Test
        fun `setup for 5 destination with different arguments`() {

            val kotlinSource = listOf(
                SourceFile.kotlin("Composables1.kt",
                    """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Home
                @Destination
                fun HomeDestination(name: String) {
                
                }
                """.trimIndent()
                ),
                SourceFile.kotlin("Composables2.kt",
                    """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Destination
                fun DetailDestination(age: Int) {
                
                }
                """.trimIndent()
                ),
                SourceFile.kotlin("Composables3.kt",
                    """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Destination
                fun UltraDestination(age: Int, float: Float?) {
                
                }
                """.trimIndent()
                ),
                SourceFile.kotlin("Composables4.kt",
                    """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Destination
                fun MyDestination(value: Int?) {
                
                }
                """.trimIndent()
                ),
                SourceFile.kotlin("Composables5.kt",
                    """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Destination
                fun NoDestination() {
                
                }
                """.trimIndent()
                ),
            ).toTypedArray()

            val compilationResult = compile(*kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng
            
            import android.util.Log
            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import androidx.navigation.NavType
            import androidx.navigation.compose.NavHost
            import androidx.navigation.compose.composable
            import androidx.navigation.navArgument
            import kotlin.String
            import kotlin.Unit
            
            @Composable
            public fun SetupNavHost(navController: NavHostController): Unit {
              NavHost(navController = navController, startDestination = "HomeDestination/argName")
              {
                composable("HomeDestination/argName", arguments = listOf(
                  navArgument("argName"){
                    nullable = false
                    type = NavType.fromArgType("String","kotlin")
                  },
                )) { backStackEntry ->
                  val argName = backStackEntry.arguments?.getString("argName")
                  requireNotNull(argName)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(name=argName)
                }
                composable("DetailDestination/argAge", arguments = listOf(
                  navArgument("argAge"){
                    nullable = false
                    type = NavType.fromArgType("Int","kotlin")
                  },
                )) { backStackEntry ->
                  val argAge = backStackEntry.arguments?.getInt("argAge")
                  requireNotNull(argAge)
                  Log.d(TAG, "Navigating to DetailDestination")
                  DetailDestination(age=argAge)
                }
                composable("UltraDestination?argAge={age}&argFloat={float}", arguments = listOf(
                  navArgument("argAge"){
                    nullable = false
                    type = NavType.fromArgType("Int","kotlin")
                  },
                  navArgument("argFloat"){
                    nullable = true
                    type = NavType.fromArgType("Float","kotlin")
                  },
                )) { backStackEntry ->
                  val argAge = backStackEntry.arguments?.getInt("argAge")
                  val argFloat = backStackEntry.arguments?.getFloat("argFloat")
                  requireNotNull(argAge)
                  Log.d(TAG, "Navigating to UltraDestination")
                  UltraDestination(age=argAge, float=argFloat)
                }
                composable("MyDestination?argValue={value}", arguments = listOf(
                  navArgument("argValue"){
                    nullable = true
                    type = NavType.fromArgType("Int","kotlin")
                  },
                )) { backStackEntry ->
                  val argValue = backStackEntry.arguments?.getInt("argValue")
                  Log.d(TAG, "Navigating to MyDestination")
                  MyDestination(value=argValue)
                }
                composable("NoDestination") {
                  Log.d(TAG, "Navigating to NoDestination")
                  NoDestination()
                }
              }
            }
            
            private const val TAG: String = "NavHost"
            """.trimIndent()

            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavHost.kt")
            )
        }

    }

    @DisplayName("Navigation function Generator")
    @Nested
    inner class NavigationFunctionGenerator {

        @Test
        fun `generate 1 navigation function entry with no arguments`() {

            val kotlinSource = SourceFile.kotlin("Composables.kt",
                """
            package de.se.cng                
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination() {
            
            }
            """.trimIndent()
            )

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng                

            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import kotlin.Unit

            public fun NavHostController.navigateToHomeDestination(): Unit {
              navigate("HomeDestination")
            }
            """.trimIndent()
            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavigationFunctions.kt")
            )
        }

        @Test
        fun `generate 1 navigation function entry with 1 non-null argument`() {

            val kotlinSource = SourceFile.kotlin("Composables.kt",
                """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Home
                @Destination
                fun HomeDestination(name: String) {
                
                }
                """.trimIndent()
            )

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng                

            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import kotlin.String
            import kotlin.Unit

            public fun NavHostController.navigateToHomeDestination(name: String): Unit {
                navigate("HomeDestination/${'$'}name")
            }
            """.trimIndent()
            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavigationFunctions.kt")
            )
        }

        @Test
        fun `generate 1 navigation function entry with 1 nullable argument`() {

            val kotlinSource = SourceFile.kotlin("Composables.kt",
                """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Home
                @Destination
                fun HomeDestination(name: String?) {
                
                }
                """.trimIndent()
            )

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng                

            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import kotlin.String
            import kotlin.Unit

            public fun NavHostController.navigateToHomeDestination(name: String?): Unit {
                navigate("HomeDestination?arg_name=${'$'}name")
            }
            """.trimIndent()
            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavigationFunctions.kt")
            )
        }

        @Test
        fun `generate 1 navigation function entry with 3 non-null arguments`() {

            val kotlinSource = SourceFile.kotlin("Composables.kt",
                """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Home
                @Destination
                fun HomeDestination(name: String, age: Int, height: Double) {
                
                }
                """.trimIndent()
            )

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng

            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import kotlin.Double
            import kotlin.Int
            import kotlin.String
            import kotlin.Unit

            public fun NavHostController.navigateToHomeDestination(
              name: String,
              age: Int,
              height: Double,
            ): Unit {
              navigate("HomeDestination/${'$'}name/${'$'}age/${'$'}height")
            }
            """.trimIndent()
            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavigationFunctions.kt")
            )
        }

        @Test
        fun `generate 1 navigation function entry with 3 nullable arguments`() {

            val kotlinSource = SourceFile.kotlin("Composables.kt",
                """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Home
                @Destination
                fun HomeDestination(name: String?, age: Int?, height: Double?) {
                
                }
                """.trimIndent()
            )

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng

            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import kotlin.Double
            import kotlin.Int
            import kotlin.String
            import kotlin.Unit
            
            public fun NavHostController.navigateToHomeDestination(
              name: String?,
              age: Int?,
              height: Double?,
            ): Unit {
              navigate("HomeDestination?arg_name=${'$'}name&arg_age=${'$'}age&arg_height=${'$'}height")
            }
            """.trimIndent()
            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavigationFunctions.kt")
            )
        }

        @Test
        fun `generate 1 navigation function entry with 4 mixed arguments`() {

            val kotlinSource = SourceFile.kotlin("Composables.kt",
                """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Home
                @Destination
                fun HomeDestination(name: String?, age: Int, height: Double?, weight: Float) {
                
                }
                """.trimIndent()
            )

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng                

            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import kotlin.String
            import kotlin.Double
            import kotlin.Float
            import kotlin.Int
            import kotlin.Unit

            public fun NavHostController.navigateToHomeDestination(
                name: String?,
                age: Int,
                height: Double?,
                weight: Float,
            ): Unit {
                navigate("HomeDestination?arg_name=${'$'}name&arg_age=${'$'}age&arg_height=${'$'}height&arg_weight=${'$'}weight")
            }
            """.trimIndent()

            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavigationFunctions.kt")
            )
        }

        @Test
        fun `generate 2 navigation functions entry with no arguments`() {

            val kotlinSource = SourceFile.kotlin("Composables.kt",
                """
            package de.se.cng                
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination() {
            
            }            
            @Composable
            @Destination(isHome = false)
            fun DetailDestination() {
            
            }
            """.trimIndent()
            )

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng                
            
            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import kotlin.Unit
            
            public fun NavHostController.navigateToHomeDestination(): Unit {
              navigate("HomeDestination")
            }
            
            public fun NavHostController.navigateToDetailDestination(): Unit {
              navigate("DetailDestination")
            }
            """.trimIndent()
            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavigationFunctions.kt")
            )
        }

        @Test
        fun `generate 2 navigation functions entry with 1 non-null argument`() {

            val kotlinSource = SourceFile.kotlin("Composables.kt",
                """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Home
                @Destination
                fun HomeDestination(name: String) {
                
                }
                
                @Composable
                @Destination
                fun DetailDestination(age: Int) {
                
                }
                """.trimIndent()
            )

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng                

            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import kotlin.Int
            import kotlin.String
            import kotlin.Unit

            public fun NavHostController.navigateToHomeDestination(name: String): Unit {
                navigate("HomeDestination/${'$'}name")
            }

            public fun NavHostController.navigateToDetailDestination(age: Int): Unit {
                navigate("DetailDestination/${'$'}age")
            }
            """.trimIndent()
            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavigationFunctions.kt")
            )
        }

        @Test
        fun `generate 2 navigation functions entry with 1 nullable argument`() {

            val kotlinSource = SourceFile.kotlin("Composables.kt",
                """
                package de.se.cng                
                
                import de.se.cng.annotation.*
                import androidx.compose.runtime.Composable
                
                @Composable
                @Home
                @Destination
                fun HomeDestination(name: String?) {
                
                }
                
                @Composable
                @Destination
                fun DetailDestination(age: Int?) {
                
                }
                """.trimIndent()
            )

            val compilationResult = compile(kotlinSource)
            assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)

            val expected = """
            package de.se.cng                
            
            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import kotlin.Int
            import kotlin.String
            import kotlin.Unit
                       
            public fun NavHostController.navigateToHomeDestination(name: String?): Unit {
                navigate("HomeDestination?arg_name=${'$'}name")
            }     

            public fun NavHostController.navigateToDetailDestination(age: Int?): Unit {
                navigate("DetailDestination?arg_age=${'$'}age")
            }
            """.trimIndent()
            assertSourceEquals(
                expected,
                compilationResult.sourceFor("NavigationFunctions.kt")
            )
        }
    }

}