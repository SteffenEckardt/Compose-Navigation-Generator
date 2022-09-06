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
        import androidx.navigation.NavOptionsBuilder
        import kotlin.String
        import kotlin.Unit
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(name: String, navOptions: (NavOptionsBuilder.() -> Unit)? =
              null): Unit {
            navHostController.navigate("HomeDestination/${'$'}name", navOptions ?: { })
          }
        
          public fun navigateHome(name: String, navOptions: (NavOptionsBuilder.() -> Unit)? = null): Unit {
            navHostController.navigate("HomeDestination/${'$'}name", navOptions ?: { })
          }
        
          public fun navigateUp(): Unit {
            navHostController.navigateUp()
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
        import androidx.navigation.NavOptionsBuilder
        import kotlin.Unit
        import kotlin.String 
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(name: String,navOptions: (NavOptionsBuilder.() -> Unit)? = 
                null): Unit {
            navHostController.navigate("HomeDestination/${'$'}name", navOptions ?: { })
          }
          public fun navigateHome(name: String,navOptions: (NavOptionsBuilder.() -> Unit)? = null): Unit {
            navHostController.navigate("HomeDestination/${'$'}name", navOptions ?: { })
          }
        
          public fun navigateUp(): Unit {
            navHostController.navigateUp()
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
        import androidx.navigation.NavOptionsBuilder
        import kotlin.String
        import kotlin.Unit
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(name: String?, navOptions: (NavOptionsBuilder.() -> Unit)? =
              null): Unit {
            navHostController.navigate("HomeDestination?argName=${'$'}name", navOptions ?: { })
          }
        
          public fun navigateHome(name: String?, navOptions: (NavOptionsBuilder.() -> Unit)? = null): Unit {
            navHostController.navigate("HomeDestination?argName=${'$'}name", navOptions ?: { })
          }
        
          public fun navigateUp(): Unit {
            navHostController.navigateUp()
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
        import androidx.navigation.NavOptionsBuilder
        import kotlin.Float
        import kotlin.Int
        import kotlin.String
        import kotlin.Unit
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(
            name: String,
            age: Int,
            height: Float,
            navOptions: (NavOptionsBuilder.() -> Unit)? = null,
          ): Unit {
            navHostController.navigate("HomeDestination/${'$'}name/${'$'}age/${'$'}height", navOptions ?: { })
          }
        
          public fun navigateHome(
            name: String,
            age: Int,
            height: Float,
            navOptions: (NavOptionsBuilder.() -> Unit)? = null,
          ): Unit {
            navHostController.navigate("HomeDestination/${'$'}name/${'$'}age/${'$'}height", navOptions ?: { })
          }
        
          public fun navigateUp(): Unit {
            navHostController.navigateUp()
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
        import androidx.navigation.NavOptionsBuilder
        import kotlin.Float
        import kotlin.Int
        import kotlin.String
        import kotlin.Unit
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(
            name: String?,
            age: Int?,
            height: Float?,
            navOptions: (NavOptionsBuilder.() -> Unit)? = null,
          ): Unit {
            navHostController.navigate("HomeDestination?argName=${'$'}name&argAge=${'$'}age&argHeight=${'$'}height",
                navOptions ?: { })
          }
        
          public fun navigateHome(
            name: String?,
            age: Int?,
            height: Float?,
            navOptions: (NavOptionsBuilder.() -> Unit)? = null,
          ): Unit {
            navHostController.navigate("HomeDestination?argName=${'$'}name&argAge=${'$'}age&argHeight=${'$'}height",
                navOptions ?: { })
          }
        
          public fun navigateUp(): Unit {
            navHostController.navigateUp()
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
        import androidx.navigation.NavOptionsBuilder
        import kotlin.Float
        import kotlin.Int
        import kotlin.String
        import kotlin.Unit
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(
            name: String?,
            age: Int,
            height: Float?,
            weight: Float,
            navOptions: (NavOptionsBuilder.() -> Unit)? = null,
          ): Unit {
            navHostController.navigate("HomeDestination?argName=${'$'}name&argAge=${'$'}age&argHeight=${'$'}height&argWeight=${'$'}weight",
                navOptions ?: { })
          }
        
          public fun navigateHome(
            name: String?,
            age: Int,
            height: Float?,
            weight: Float,
            navOptions: (NavOptionsBuilder.() -> Unit)? = null,
          ): Unit {
            navHostController.navigate("HomeDestination?argName=${'$'}name&argAge=${'$'}age&argHeight=${'$'}height&argWeight=${'$'}weight",
                navOptions ?: { })
          }
        
          public fun navigateUp(): Unit {
            navHostController.navigateUp()
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
        import androidx.navigation.NavOptionsBuilder
        import kotlin.Int
        import kotlin.String
        import kotlin.Unit
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(name: String, navOptions: (NavOptionsBuilder.() -> Unit)? =
              null): Unit {
            navHostController.navigate("HomeDestination/${'$'}name", navOptions ?: { })
          }
        
          public fun navigateToDetailDestination(age: Int, navOptions: (NavOptionsBuilder.() -> Unit)? =
              null): Unit {
            navHostController.navigate("DetailDestination/${'$'}age", navOptions ?: { })
          }
        
          public fun navigateHome(name: String, navOptions: (NavOptionsBuilder.() -> Unit)? = null): Unit {
            navHostController.navigate("HomeDestination/${'$'}name", navOptions ?: { })
          }
        
          public fun navigateUp(): Unit {
            navHostController.navigateUp()
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
        import androidx.navigation.NavOptionsBuilder
        import kotlin.Int
        import kotlin.String
        import kotlin.Unit
        
        public class Navigator(
          private val navHostController: NavHostController,
        ) {
          public fun navigateToHomeDestination(name: String?, navOptions: (NavOptionsBuilder.() -> Unit)? =
              null): Unit {
            navHostController.navigate("HomeDestination?argName=${'$'}name", navOptions ?: { })
          }
        
          public fun navigateToDetailDestination(age: Int?, navOptions: (NavOptionsBuilder.() -> Unit)? =
              null): Unit {
            navHostController.navigate("DetailDestination?argAge=${'$'}age", navOptions ?: { })
          }
        
          public fun navigateHome(name: String?, navOptions: (NavOptionsBuilder.() -> Unit)? = null): Unit {
            navHostController.navigate("HomeDestination?argName=${'$'}name", navOptions ?: { })
          }
        
          public fun navigateUp(): Unit {
            navHostController.navigateUp()
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