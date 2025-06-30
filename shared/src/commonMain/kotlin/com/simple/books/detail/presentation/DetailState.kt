package com.simple.books.detail.presentation

import com.simple.books.detail.application.BookDetail

class DetailState (
    val book: BookDetail? = null,
    val loading: Boolean = false,
    val error: String? = null
)
