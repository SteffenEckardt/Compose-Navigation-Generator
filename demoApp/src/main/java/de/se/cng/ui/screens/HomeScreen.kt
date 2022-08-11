package de.se.cng.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import de.se.cng.ui.navigation.NavigationDestination.*

@Composable
fun HomeScreen(navHostController: NavHostController) = TemplateScreen(
    title = "Home",
    navController = navHostController,
    navigationDestinationTitle = "List",
    navigationDestination = ListDestination
)
