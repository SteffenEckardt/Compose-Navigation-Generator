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

import de.se.cng.processor.ProcessorTestBase
import de.se.cng.processor.generator.Dummies.Destinations.details
import de.se.cng.processor.generator.Dummies.Destinations.home
import de.se.cng.processor.generator.Dummies.Parameters.age
import de.se.cng.processor.generator.Dummies.Parameters.height
import de.se.cng.processor.generator.Dummies.Parameters.name
import de.se.cng.processor.generator.Dummies.Parameters.weight
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.processor.DestinationAnnotationProcessor
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Navigation function Generator")
class NavigatorClassGeneratorTest : ProcessorTestBase() {

    companion object {
        private const val PACKAGE = DestinationAnnotationProcessor.PACKAGE
    }

    @Test
    fun `ignore single NavHostController parameter`() {
        val navigatorClassFile = configureSource(home())

        val expected = """
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

        assertSourceEquals(expected, navigatorClassFile)
    }

    @Test
    fun `ignore NavHostController as one of multiple parameters`() {
        val navigatorClassFile = configureSource(home(name()))

        val expected = """
        package $PACKAGE
        
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import kotlin.String
        import kotlin.Unit
        
        public class Navigator(
            private val navHostController: NavHostController,
        ) {
            public fun navigateToHomeDestination(name: String): Unit {
                navHostController.navigate("HomeDestination/${'$'}name")
            }
        }
        """.trimIndent()

        assertSourceEquals(expected, navigatorClassFile)
    }

    @Test
    fun `generate 1 navigation function entry with no arguments`() {
        val navigatorClassFile = configureSource(home())

        val expected = """
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

        assertSourceEquals(
            expected,
            navigatorClassFile
        )
    }

    @Test
    fun `generate 1 navigation function entry with 1 non-null argument`() {
        val navigatorClassFile = configureSource(home(name()))

        val expected = """
        package $PACKAGE
        
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import kotlin.Unit
        import kotlin.String 
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(name: String): Unit {
            navHostController.navigate("HomeDestination/${'$'}name")
          }
        }
        """.trimIndent()

        assertSourceEquals(
            expected,
            navigatorClassFile
        )
    }

    @Test
    fun `generate 1 navigation function entry with 1 nullable argument`() {
        val navigatorClassFile = configureSource(home(name(true)))

        val expected = """
        package $PACKAGE
        
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import kotlin.Unit
        import kotlin.String 
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(name: String?): Unit {
            navHostController.navigate("HomeDestination?argName=${'$'}name")
          }
        }
        """.trimIndent()

        assertSourceEquals(
            expected,
            navigatorClassFile
        )
    }

    @Test
    fun `generate 1 navigation function entry with 3 non-null arguments`() {
        val navigatorClassFile = configureSource(home(name(), age(), height()))

        val expected = """
        package $PACKAGE
        
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import kotlin.Unit
        import kotlin.String 
        import kotlin.Int 
        import kotlin.Float 
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(
            name: String, 
            age: Int, 
            height: Float,
            ): Unit {
            navHostController.navigate("HomeDestination/${'$'}name/${'$'}age/${'$'}height")
          }
        }
        """.trimIndent()

        assertSourceEquals(
            expected,
            navigatorClassFile
        )
    }

    @Test
    fun `generate 1 navigation function entry with 3 nullable arguments`() {
        val navigatorClassFile = configureSource(home(name(true), age(true), height(true)))

        val expected = """
        package $PACKAGE
        
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import kotlin.Unit
        import kotlin.String 
        import kotlin.Int 
        import kotlin.Float 
        
        public class Navigator(
            private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(
                name: String?, 
                age: Int?, 
                height: Float?,
          ): Unit {
                navHostController.navigate("HomeDestination?argName=${'$'}name&argAge=${'$'}age&argHeight=${'$'}height")
          }
        }
        """.trimIndent()

        assertSourceEquals(
            expected,
            navigatorClassFile
        )
    }

    @Test
    fun `generate 1 navigation function entry with 4 mixed arguments`() {
        val navigatorClassFile = configureSource(
            home(
                name(true),
                age(false),
                height(true),
                weight(false)
            )
        )

        val expected = """
        package $PACKAGE
        
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import kotlin.Unit
        import kotlin.String 
        import kotlin.Int 
        import kotlin.Float 
        
        public class Navigator(
            private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(             
                name: String?,
                age: Int,
                height: Float?,
                weight: Float,
          ): Unit {
                navHostController.navigate("HomeDestination?argName=${'$'}name&argAge=${'$'}age&argHeight=${'$'}height&argWeight=${'$'}weight")
          }
        }
        """.trimIndent()

        assertSourceEquals(
            expected,
            navigatorClassFile
        )
    }

    @Test
    fun `generate 2 navigation functions entry with no arguments`() {

        val navigatorClassFile = configureSource(
            home(),
            details()
        )

        val expected = """
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
          public fun navigateToDetailDestination(): Unit {
            navHostController.navigate("DetailDestination")
          }
        }
        """.trimIndent()

        assertSourceEquals(
            expected,
            navigatorClassFile
        )
    }

    @Test
    fun `generate 2 navigation functions entry with 1 non-null argument`() {
        val navigatorClassFile = configureSource(
            home(name()),
            details(age())
        )

        val expected = """
        package $PACKAGE
        
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import kotlin.Unit
        import kotlin.String
        import kotlin.Int
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(name: String): Unit {
            navHostController.navigate("HomeDestination/${'$'}name")
          }
          public fun navigateToDetailDestination(age: Int): Unit {
            navHostController.navigate("DetailDestination/${'$'}age")
          }
        }
        """.trimIndent()

        assertSourceEquals(
            expected,
            navigatorClassFile
        )
    }


    @Test
    fun `generate 2 navigation functions entry with 1 nullable argument`() {
        val navigatorClassFile = configureSource(
            home(name(true)),
            details(age(true))
        )

        val expected = """
        package $PACKAGE
        
        import androidx.compose.runtime.Composable
        import androidx.navigation.NavHostController
        import kotlin.Unit
        import kotlin.String
        import kotlin.Int
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(name: String?): Unit {
            navHostController.navigate("HomeDestination?argName=${'$'}name")
          }
          public fun navigateToDetailDestination(age: Int?): Unit {
            navHostController.navigate("DetailDestination?argAge=${'$'}age")
          }
        }
        """.trimIndent()

        assertSourceEquals(
            expected,
            navigatorClassFile
        )
    }

    private fun configureSource(vararg destinations: NavigationDestination) =
        generateNavigatorFile(PACKAGE, destinations.toList(), false)
            .toString()

}