package de.se.cng.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import de.se.cng.annotation.Destination
import de.se.cng.navigateToDetailScreen

@Destination
@Composable
fun ListScreen(navHostController: NavHostController) = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceAround,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(text = "List")
    Button(onClick = { navHostController.navigateToDetailScreen(1, "Hello1", null, "") }) {
        Text(text = "Detail-1")
    }
    Button(onClick = { navHostController.navigateToDetailScreen(2, "Hello2", "World", "") }) {
        Text(text = "Detail-2")
    }
    Button(onClick = { navHostController.navigateToDetailScreen(3, "Hello3", "World", "Mars") }) {
        Text(text = "Detail-3")
    }
    Button(onClick = { navHostController.navigateToDetailScreen(4, "Hello4", param4 = "Mars", param3 = null) }) {
        Text(text = "Detail-4")
    }
}