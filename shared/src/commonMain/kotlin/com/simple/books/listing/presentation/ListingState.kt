package com.simple.books.listing.presentation

import com.simple.books.listing.application.Book

data class ListingState(
    val books: List<Book> = listOf(),
    val query: String = "hello",
    val loading: Boolean = false,
    val error: String? = null
)
