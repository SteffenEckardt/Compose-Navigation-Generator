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
import de.se.cng.processor.models.NavigationDestination
import de.se.cng.processor.processor.DestinationAnnotationProcessor
import org.junit.jupiter.api.Test

class NavigatorClassGeneratorTest : ProcessorTestBase() {

    companion object {
        private const val PACKAGE = DestinationAnnotationProcessor.PACKAGE
    }

    @Test
    fun `ignore single Navigator parameter`() {
        val destinations = SourceFactory {
            Home {
                Navigator()
            }
        }

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

        assertSourceEquals(expected, destinations.buildSource())
    }

    @Test
    fun `ignore Navigator as one of multiple parameters`() {
        val destinations = SourceFactory {
            Home {
                Name()
                Navigator()
            }
        }

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

        assertSourceEquals(expected, destinations.buildSource())
    }

    @Test
    fun `generate 1 navigation function entry with no arguments`() {
        val destinations = SourceFactory {
            Home { }
        }

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
            destinations.buildSource()
        )
    }

    @Test
    fun `generate 1 navigation function entry with 1 non-null argument`() {
        val destinations = SourceFactory {
            Home {
                Name()
            }
        }

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
            destinations.buildSource()
        )
    }

    @Test
    fun `generate 1 navigation function entry with 1 nullable argument`() {
        val destinations = SourceFactory {
            Home {
                Name(true)
            }
        }

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
            destinations.buildSource()
        )
    }

    @Test
    fun `generate 1 navigation function entry with 3 non-null arguments`() {
        val destinations = SourceFactory {
            Home {
                Name()
                Age()
                Height()
            }
        }

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
            destinations.buildSource()
        )
    }

    @Test
    fun `generate 1 navigation function entry with 3 nullable arguments`() {
        val destinations = SourceFactory {
            Home {
                Name(true)
                Age(true)
                Height(true)
            }
        }

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
            destinations.buildSource()
        )
    }

    @Test
    fun `generate 1 navigation function entry with 4 mixed arguments`() {
        val destinations = SourceFactory {
            Home {
                Name(true)
                Age(false)
                Height(true)
                Weight(false)
            }
        }


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
            destinations.buildSource()
        )
    }

    @Test
    fun `generate 2 navigation functions entry with no arguments`() {
        val destinations = SourceFactory {
            Home { }
            Details { }
        }


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
            destinations.buildSource()
        )
    }

    @Test
    fun `generate 2 navigation functions entry with 1 non-null argument`() {
        val destinations = SourceFactory {
            Home {
                Name()
            }
            Details {
                Age()
            }
        }


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
            destinations.buildSource()
        )
    }


    @Test
    fun `generate 2 navigation functions entry with 1 nullable argument`() {
        val destinations = SourceFactory {
            Home { Name(true) }
            Details { Age(true) }
        }

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
            destinations.buildSource()
        )
    }

    private fun List<NavigationDestination>.buildSource(): String {
        return generateNavigatorFile(PACKAGE, this, false).toString()
    }

}