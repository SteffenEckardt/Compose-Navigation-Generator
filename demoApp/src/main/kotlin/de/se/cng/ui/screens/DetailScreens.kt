package de.se.cng.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Start
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import de.se.cng.annotation.Destination
import de.se.cng.generated.Navigator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Template(title: String, navigator: Navigator, displayValues: () -> List<Pair<String, Any?>> = { emptyList() }) = Scaffold(topBar = {
    SmallTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { navigator.navigateHome() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Navigate Back")
            }
        }
    )
}
) {
    Box(Modifier.padding(it)) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            displayValues().forEach { (key, value) ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Start) {
                    Text("Key: \"$key\" - Value: \"$value\"")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Destination
@Composable
fun NoArguments(navigator: Navigator) = Template(title = "No Arguments", navigator = navigator)

@Destination
@Composable
fun SingleNonNullableArgument(navigator: Navigator, name1: String) = Template(title = "Single Non-Null", navigator = navigator) {
    listOf("name" to name1)
}

@Destination
@Composable
fun SingleNullableArgument(navigator: Navigator, name: String?) = Template(title = "Single Non-Null", navigator = navigator) {
    listOf("name?" to name)
}

@Destination
@Composable
fun MultipleNonNullableArguments(navigator: Navigator, name: String, age: Int, height: Float) = Template(title = "Single Non-Null", navigator = navigator) {
    listOf(
        "name" to name,
        "age" to age,
        "height" to height
    )
}

@Destination
@Composable
fun MultipleNullableArguments(navigator: Navigator, name: String?, age: String?, height: String?) = Template(title = "Single Non-Null", navigator = navigator) {
    listOf(
        "name?" to name,
        "age?" to age,
        "height?" to height,
    )
}

@Destination
@Composable
fun MultipleMixedArguments(navigator: Navigator, name: String?, age: Int, height: Float, weight: String?) = Template(title = "Single Non-Null", navigator = navigator) {
    listOf(
        "name?" to name,
        "age" to age,
        "height" to height,
        "weight?" to weight,
    )
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun TemplatePreview() = Template(title = "Dummy", navigator = Navigator(rememberNavController())) {
    listOf(
        "string" to "String",
        "int" to 999,
        "float?" to null
    )
}