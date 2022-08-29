package de.se.cng.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import de.se.cng.annotation.Destination
import de.se.cng.annotation.Home
import de.se.cng.navigateToListScreen

@Home
@Destination
@Composable
fun HomeScreen(navHostController: NavHostController) = TemplateScreen(
    title = "Home",
    navigationDestinationTitle = "List",
) {
    navHostController.navigateToListScreen()
}
