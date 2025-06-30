package com.simple.books.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.navigation.NavHostController
import com.simple.books.ui.navigation.AppNavHost

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainScreen(navController: NavHostController,) {
    AppNavHost(
        navController = navController
    )
}