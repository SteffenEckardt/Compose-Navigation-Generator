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
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.processor.DestinationAnnotationProcessor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Navigation Function Generator")
class SetupNavHostGeneratorTest : ProcessorTestBase() {

    companion object {
        private const val PACKAGE = DestinationAnnotationProcessor.PACKAGE
    }

    @Test
    fun `string type is mapped correctly`() {
        val typeName = "String"
        val destinations = SourceFactory {
            Home {
                Parameter("arg", ParameterType.String)
            }
        }

        val expected = """
            package $PACKAGE
            
            import android.util.Log
            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import androidx.navigation.NavType
            import androidx.navigation.compose.NavHost
            import androidx.navigation.compose.composable
            import androidx.navigation.navArgument
            import de.se.ui.HomeDestination
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
                  HomeDestination(arg=argArg)
                }
              }
            }
            """.trimIndent()

        assertSourceEquals(expected, destinations.buildSource())
    }

    @Test
    fun `int type is mapped correctly`() {
        val typeName = "Int"
        val destinations = SourceFactory {
            Home {
                Parameter("arg", ParameterType.Int)
            }
        }

        val expected = """
            package $PACKAGE
            
            import android.util.Log
            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import androidx.navigation.NavType
            import androidx.navigation.compose.NavHost
            import androidx.navigation.compose.composable
            import androidx.navigation.navArgument
            import de.se.ui.HomeDestination
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
                  HomeDestination(arg=argArg)
                }
              }
            }
            """.trimIndent()

        assertSourceEquals(expected, destinations.buildSource())
    }

    @Test
    fun `float type is mapped correctly`() {
        val typeName = "Float"
        val destinations = SourceFactory {
            Home {
                Parameter("arg", ParameterType.Float)
            }
        }

        val expected = """
            package $PACKAGE
            
            import android.util.Log
            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import androidx.navigation.NavType
            import androidx.navigation.compose.NavHost
            import androidx.navigation.compose.composable
            import androidx.navigation.navArgument
            import de.se.ui.HomeDestination
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
                  HomeDestination(arg=argArg)
                }
              }
            }            
            """.trimIndent()

        assertSourceEquals(expected, destinations.buildSource())
    }

    @Test
    fun `long type is mapped correctly`() {
        val typeName = "Long"
        val destinations = SourceFactory {
            Home {
                Parameter("arg", ParameterType.Long)
            }
        }

        val expected = """
            package $PACKAGE
            
            import android.util.Log
            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import androidx.navigation.NavType
            import androidx.navigation.compose.NavHost
            import androidx.navigation.compose.composable
            import androidx.navigation.navArgument
            import de.se.ui.HomeDestination
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
                  HomeDestination(arg=argArg)
                }
              }
            }
            """.trimIndent()

        assertSourceEquals(expected, destinations.buildSource())
    }

    @Test
    fun `boolean type is mapped correctly`() {
        val typeName = "Bool"
        val destinations = SourceFactory {
            Home {
                Parameter("arg", ParameterType.Boolean)
            }
        }

        val expected = """
        package $PACKAGE
        
        import android.util.Log
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import androidx.navigation.NavType
        import androidx.navigation.compose.NavHost
        import androidx.navigation.compose.composable
        import androidx.navigation.navArgument
        import de.se.ui.HomeDestination
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
              HomeDestination(arg=argArg)
            }
          }
        }
        """.trimIndent()

        assertSourceEquals(expected, destinations.buildSource())
    }

    @Test
    fun `double type is mapped correctly (via float)`() {
        val destinations = SourceFactory {
            Home {
                Parameter("arg", ParameterType.Double)
            }
        }

        val expected = """
        package $PACKAGE
        
        import android.util.Log
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import androidx.navigation.NavType
        import androidx.navigation.compose.NavHost
        import androidx.navigation.compose.composable
        import androidx.navigation.navArgument
        import de.se.ui.HomeDestination
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
              HomeDestination(arg=argArg)
            }
          }
        }
        """.trimIndent()

        assertSourceEquals(expected, destinations.buildSource())
    }

    @Test
    fun `short type is mapped correctly (via int)`() {
        val typeName = "Short"
        val destinations = SourceFactory {
            Home {
                Parameter("arg", ParameterType.Short)
            }
        }

        val expected = """
        package $PACKAGE
        
        import android.util.Log
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import androidx.navigation.NavType
        import androidx.navigation.compose.NavHost
        import androidx.navigation.compose.composable
        import androidx.navigation.navArgument
        import de.se.ui.HomeDestination
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
              val argArg = backStackEntry.arguments?.get${typeName}("argArg")
              requireNotNull(argArg)
              HomeDestination(arg=argArg)
            }
          }
        }
        """.trimIndent()

        assertSourceEquals(expected, destinations.buildSource())
    }

    @Test
    fun `byte type is mapped correctly (via int)`() {
        val typeName = "Byte"
        val destinations = SourceFactory {
            Home {
                Parameter("arg", ParameterType.Byte)
            }
        }

        val expected = """
            package $PACKAGE
            
            import android.util.Log
            import androidx.compose.runtime.Composable
            import androidx.navigation.NavHostController
            import androidx.navigation.NavType
            import androidx.navigation.compose.NavHost
            import androidx.navigation.compose.composable
            import androidx.navigation.navArgument
            import de.se.ui.HomeDestination
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
                  val argArg = backStackEntry.arguments?.get${typeName}("argArg")
                  requireNotNull(argArg)
                  HomeDestination(arg=argArg)
                }
              }
            }
            """.trimIndent()

        assertSourceEquals(expected, destinations.buildSource())
    }

    @Test
    fun `char type is mapped correctly (via int)`() {
        val typeName = "Char"
        val destinations = SourceFactory {
            Home {
                Parameter("arg", ParameterType.Char)
            }
        }

        val expected = """
        package $PACKAGE
        
        import android.util.Log
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import androidx.navigation.NavType
        import androidx.navigation.compose.NavHost
        import androidx.navigation.compose.composable
        import androidx.navigation.navArgument
        import de.se.ui.HomeDestination
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
              val argArg = backStackEntry.arguments?.get${typeName}("argArg")
              requireNotNull(argArg)
              HomeDestination(arg=argArg)
            }
          }
        }
        """.trimIndent()

        assertSourceEquals(expected, destinations.buildSource())
    }

    private fun List<NavigationDestination>.buildSource(): String {
        return generateSetupFile(PACKAGE, this, false).toString()
    }
}