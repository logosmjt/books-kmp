package com.simple.books.config

import io.kamel.core.applicationContext
import io.kamel.core.config.KamelConfigBuilder
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.config.resourcesIdMapper
import kotlin.let

internal actual fun KamelConfigBuilder.platformSpecificConfig() {
    if (applicationContext == null) {
        println("Warning: Android application context is not provided. Skipping adding Kamel Components requiring Android application context.")
    }

    applicationContext?.applicationContext?.let { context ->
        resourcesIdMapper(context)
        resourcesFetcher(context)
    }
}
