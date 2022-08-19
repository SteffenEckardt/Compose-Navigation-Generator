package de.se.cng.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import de.se.cng.ui.navigation.NavigationDestination.HomeDestination

@Composable
fun DetailScreen(
    navHostController: NavHostController,
    elementId: Int,
    param2: String,
    param3: String?,
    param4: String = "ASDF"
) = TemplateScreen(
    title = """Detail ($elementId)
        | Param-2 = $param2
        | Param-3 = $param3
        | Param-4 = $param4
    """.trimMargin(),
    navController = navHostController,
    navigationDestinationTitle = "Home",
    navigationDestination = HomeDestination
)


