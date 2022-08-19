package de.se.cng.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import de.se.cng.annotation.*
import de.se.cng.ui.navigation.NavigationDestination.*
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun HomeScreen(navHostController: NavHostController) = TemplateScreen(
    title = "Home",
    navController = navHostController,
    navigationDestinationTitle = "List",
    navigationDestination = ListDestination
)

@Destination
@Home
@Composable
fun Home2() = Column(Modifier.fillMaxSize(), horizontalAlignment = CenterHorizontally, verticalArrangement = Arrangement.Center) {
    Text(text = "HOME")
}


@Composable
fun asdsd(navHostController: NavHostController) {

}