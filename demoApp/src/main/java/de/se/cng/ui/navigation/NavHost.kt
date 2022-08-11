package de.se.cng.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import de.se.cng.ui.navigation.NavigationDestination.*
import de.se.cng.ui.screens.DetailScreen
import de.se.cng.ui.screens.HomeScreen
import de.se.cng.ui.screens.ListScreen

private object NavHostConstants {
    const val DetailDestination_param_1 = "param1"
    const val DetailDestination_param_2 = "param2"
    const val DetailDestination_param_3 = "param3"
    const val DetailDestination_param_4 = "param4"
    const val DetailDestination_param_4_DEFAULT = "TODO-DEFAULT"
}

@Composable
fun SetupNavHost(navController: NavHostController) = NavHost(navController = navController, startDestination = HomeDestination.route.asString()) {
    val homeRoute = HomeDestination.route.asString()
    val listRoute = ListDestination.route.asString()
    val detailRoute = DetailDestination.route.asString()

    composable(homeRoute) {
        Log.d(TAG, "SetupNavHost: homeRoute=$homeRoute")
        HomeScreen(navController)
    }
    composable(listRoute) {
        Log.d(TAG, "SetupNavHost: listRoute=$listRoute")
        ListScreen(navController)
    }
    composable(detailRoute, listOf(
        navArgument(NavHostConstants.DetailDestination_param_1) {
            this.type = NavType.IntType
            this.nullable = false
        },
        navArgument(NavHostConstants.DetailDestination_param_2) {
            this.type = NavType.StringType
            this.nullable = false
        },
        navArgument(NavHostConstants.DetailDestination_param_3) {
            this.type = NavType.StringType
            this.nullable = true
        },
        navArgument(NavHostConstants.DetailDestination_param_4) {
            this.type = NavType.StringType
            this.nullable = false
            this.defaultValue = "Hello World"
        }
    )
    ) {
        Log.d(TAG, "SetupNavHost: detailRoute=$detailRoute")

        val elementParam1 = it.arguments?.getInt(NavHostConstants.DetailDestination_param_1)
        val elementParam2 = it.arguments?.getString(NavHostConstants.DetailDestination_param_2)
        val elementParam3 = it.arguments?.getString(NavHostConstants.DetailDestination_param_3)
        val elementParam4 = it.arguments?.getString(NavHostConstants.DetailDestination_param_4)


        requireNotNull(elementParam1)
        requireNotNull(elementParam2)
        requireNotNull(elementParam4)

        DetailScreen(navController, elementParam1, elementParam2, elementParam3, elementParam4)
    }
}

// TODO: GENERATED
fun NavHostController.navigateToDetails(
    id: Int,
    parameter2: String,
    parameter3: String? = null,
    parameter4: String = NavHostConstants.DetailDestination_param_4_DEFAULT
) {
    require(DetailDestination.route is Route.OptionalRoute)

    val navigationRoute = DetailDestination.route.asNavigation(
        NavHostConstants.DetailDestination_param_1 to id,
        NavHostConstants.DetailDestination_param_2 to parameter2,
        NavHostConstants.DetailDestination_param_3 to parameter3,
        NavHostConstants.DetailDestination_param_4 to parameter4,
    )

    Log.d(TAG, "navigateToDetails: route=$navigationRoute")

    navigate(navigationRoute)
}

// TODO: GENERATED
fun NavHostController.navigateToList() {
    require(ListDestination.route is Route.SimpleRoute)

    val navigationRoute = ListDestination.route.asNavigation()

    Log.d(TAG, "navigateToDetails: route=$navigationRoute")

    navigate(navigationRoute)
}

private const val TAG = "NavHost"