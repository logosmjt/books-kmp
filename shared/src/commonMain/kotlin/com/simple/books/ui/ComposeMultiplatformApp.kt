package com.simple.books.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.simple.books.ui.screens.MainScreen
import org.koin.compose.getKoin
import org.koin.core.Koin

@Composable
fun App(koin: Koin = getKoin()) {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            MainScreen(
                navController = navController,
            )
        }
    }
}
