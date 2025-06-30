package com.simple.books

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.simple.books.ui.App
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.remember
import com.simple.books.config.Default
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.LocalKamelConfig

@OptIn(ExperimentalComposeApi::class)
fun MainViewController() = ComposeUIViewController {
    val kamelConfig = remember {
        KamelConfig {
            takeFrom(KamelConfig.Default)
        }
    }

    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
        App()
    }
}
