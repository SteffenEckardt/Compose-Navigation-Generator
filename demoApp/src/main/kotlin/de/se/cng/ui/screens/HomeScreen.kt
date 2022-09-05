package de.se.cng.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import de.se.cng.annotation.Destination
import de.se.cng.annotation.Home
import de.se.cng.generated.Navigator
import de.se.cng.ui.theme.DemoAppTheme
import java.util.*
import kotlin.random.Random

private val random = Random(Date().time)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
@Home
fun HomeScreen(navigator: Navigator) = Scaffold(
    topBar = {
        SmallTopAppBar(
            title = { Text("Home", fontSize = MaterialTheme.typography.headlineMedium.fontSize) },
            navigationIcon = { IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Home Icon") }
            }
        )
    }
) {
    Box(Modifier.padding(it)) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            columns = GridCells.Fixed(2),
        ) {

            item {
                NavigationCard(title = "No Arguments", icon = Icons.Default.HourglassEmpty) {
                    navigator.navigateToNoArguments()
                }
            }

            item {
                NavigationCard(title = "Single Non-Null", icon = Icons.Default.ControlPoint) {
                    navigator.navigateToSingleNonNullableArgument("I am a Name")
                }
            }

            item {
                NavigationCard(title = "Single Nullable", icon = Icons.Default.NoiseControlOff) {
                    navigator.navigateToSingleNullableArgument(if (random.nextBoolean()) "Maybe a name" else null)
                }
            }

            item {
                NavigationCard(title = "Multiple Non-Null", icon = Icons.Default.SecurityUpdateWarning) {
                    navigator.navigateToMultipleNonNullableArguments("Name", 24, 1.85f)
                }
            }

            item {
                NavigationCard(title = "Multiple Nullable", icon = Icons.Default.MultipleStop) {
                    navigator.navigateToMultipleNullableArguments(
                        name = if (random.nextBoolean()) "Maybe a name" else null,
                        age = if (random.nextBoolean()) "24" else null,
                        height = if (random.nextBoolean()) "12.3f" else null,
                    )
                }
            }

            item {
                NavigationCard(title = "Multiple Mixed", icon = Icons.Default.Groups) {
                    navigator.navigateToMultipleMixedArguments(
                        name = if (random.nextBoolean()) "Maybe a name" else null,
                        age = 24,
                        height = 1.85f,
                        weight = if (random.nextBoolean()) "9999.99f" else null,
                    )
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationCard(title: String, icon: ImageVector, onClick: () -> Unit) = Card(onClick = onClick) {
    Box(Modifier
        .padding(8.dp)
        .fillMaxSize()) {
        Column(modifier = Modifier
            .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .width(64.dp)
                    .aspectRatio(1f),
                imageVector = icon,
                contentDescription = title
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = MaterialTheme.typography.headlineSmall.fontSize, textAlign = TextAlign.Center)
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() = DemoAppTheme {
    Surface {
        HomeScreen(navigator = Navigator(rememberNavController()))
    }
}

@Preview(device = "spec:shape=Normal,width=500,height=500,unit=px,dpi=440")
@Composable
fun NavigationCardPreview() = DemoAppTheme {
    Surface {
        NavigationCard(title = "My Card", icon = Icons.Default.Home) {

        }
    }
}
