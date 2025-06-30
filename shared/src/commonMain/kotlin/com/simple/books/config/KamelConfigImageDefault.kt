package com.simple.books.config

import io.kamel.core.config.Core
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.KamelConfigBuilder
import io.kamel.core.config.takeFrom
import io.kamel.image.config.animatedImageDecoder
import io.kamel.image.config.imageBitmapDecoder
import io.kamel.image.config.imageVectorDecoder
import io.kamel.image.config.svgDecoder

public val KamelConfig.Companion.Default: KamelConfig
    get() = KamelConfig {
        takeFrom(KamelConfig.Core)
        imageBitmapDecoder()
        imageVectorDecoder()
        svgDecoder()
        animatedImageDecoder()
        platformSpecificConfig()
    }

internal expect fun KamelConfigBuilder.platformSpecificConfig()
