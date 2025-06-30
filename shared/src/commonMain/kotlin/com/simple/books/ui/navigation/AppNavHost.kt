package com.simple.books.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.simple.books.ui.screens.AboutScreen
import com.simple.books.ui.screens.DetailScreen
import com.simple.books.ui.screens.ListingScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destinations.Listing
    ) {
        composable<Destinations.Listing> {
            ListingScreen(
                navController = navController
            )
        }

        composable<Destinations.About> {
            AboutScreen(
                navController = navController
            )
        }

        composable<Destinations.Detail> { backStackEntry ->
            val detail: Destinations.Detail = backStackEntry.toRoute()
            DetailScreen(navController = navController, id = detail.id, title = detail.title)
        }
    }
}