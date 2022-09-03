/*
    Why are these warnings ignored?
    ======================================================================================
    "RedundantVisibilityModifier"   -> KotlinPoet generates explicit public functions.
    "RedundantUnitReturnType"       -> KotlinPoet generates explicit Unit return type.
    "TestFunctionName"              -> Compose functions start with an uppercase Letter.
    "unused"                        -> Navigation functions are not called in test context.
*/
@file:Suppress("RedundantVisibilityModifier", "TestFunctionName", "RedundantUnitReturnType", "unused")

package de.se.cng.processor.generator

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import de.se.cng.processor.ProcessorTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Navigation Function Generator")
internal class NavigatorClassGeneratorTestTest : ProcessorTestBase() {

    @DisplayName("Primitives mapper")
    @Nested
    inner class Primitives {

        @Test
        fun `string type is mapped correctly`() {
            val typeName = "String"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.${typeName}Type
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.get${typeName}("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `int type is mapped correctly`() {
            val typeName = "Int"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.${typeName}Type
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.get${typeName}("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `float type is mapped correctly`() {
            val typeName = "Float"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.${typeName}Type
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.get${typeName}("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `long type is mapped correctly`() {
            val typeName = "Long"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.${typeName}Type
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.get${typeName}("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `boolean type is mapped correctly`() {
            val typeName = "Boolean"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.BoolType
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.getBool("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `double type is mapped correctly (via float)`() {
            val typeName = "Double"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.FloatType
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.getDouble("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `short type is mapped correctly (via int)`() {
            val typeName = "Short"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.IntType
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.getShort("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `byte type is mapped correctly (via int)`() {
            val typeName = "Byte"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.IntType
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.getByte("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `char type is mapped correctly (via int)`() {
            val typeName = "Char"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.IntType
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.getChar("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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

    @DisplayName("Array/Collection mapper")
    @Nested
    inner class Collections {

        @Test
        fun `string array type is mapped correctly`() {
            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: List<String>) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.StringArrayType
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.getStringArray("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `int type is mapped correctly`() {
            val typeName = "Int"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.${typeName}Type
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.get${typeName}("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `float type is mapped correctly`() {
            val typeName = "Float"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.${typeName}Type
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.get${typeName}("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `long type is mapped correctly`() {
            val typeName = "Long"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.${typeName}Type
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.get${typeName}("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `boolean type is mapped correctly`() {
            val typeName = "Boolean"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.BoolType
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.getBool("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `double type is mapped correctly (via float)`() {
            val typeName = "Double"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.FloatType
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.getDouble("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `short type is mapped correctly (via int)`() {
            val typeName = "Short"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.IntType
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.getShort("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `byte type is mapped correctly (via int)`() {
            val typeName = "Byte"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.IntType
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.getByte("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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
        fun `char type is mapped correctly (via int)`() {
            val typeName = "Char"

            val kotlinSource = SourceFile.kotlin("Composables1.kt",
                """
            package de.se.cng.generated               
            
            import de.se.cng.annotation.*
            import androidx.compose.runtime.Composable
            
            @Composable
            @Home
            @Destination
            fun HomeDestination(arg: ${typeName}) {
            
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
              NavHost(navController = navController, startDestination = "HomeDestination/argArg")
              {
                composable("HomeDestination/argArg", arguments = listOf(
                  navArgument("argArg"){
                    nullable = false
                    type = NavType.IntType
                  },
                )) { backStackEntry ->
                  val argArg = backStackEntry.arguments?.getChar("argArg")
                  requireNotNull(argArg)
                  Log.d(TAG, "Navigating to HomeDestination")
                  HomeDestination(arg=argArg)
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


}