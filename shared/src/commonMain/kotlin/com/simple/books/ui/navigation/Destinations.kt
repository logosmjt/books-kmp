package com.simple.books.ui.navigation

import kotlinx.serialization.Serializable

sealed class Destinations {
    @Serializable
    object Listing

    @Serializable
    data class Detail(val id: String, val title: String?)

    @Serializable
    object About
}