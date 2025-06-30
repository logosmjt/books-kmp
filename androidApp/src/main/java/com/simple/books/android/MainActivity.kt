package com.simple.books.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.simple.books.config.Default
import io.kamel.core.config.takeFrom
import com.simple.books.ui.App
import com.simple.books.ui.MyApplicationTheme
import com.simple.books.ui.screens.MainScreen
import io.kamel.core.ExperimentalKamelApi
import io.kamel.core.config.KamelConfig
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.imageBitmapResizingDecoder
import io.kamel.image.config.resourcesFetcher

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalKamelApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val kamelConfig = remember {
                KamelConfig {
                    takeFrom(KamelConfig.Default)
                    resourcesFetcher(this@MainActivity)
                    imageBitmapResizingDecoder()
                }
            }
            CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
                MyApplicationTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        App()
                    }
                }
            }
        }
    }
}
