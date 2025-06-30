package com.simple.books.detail.application

data class BookDetail(
    val title: String,
    val subtitle: String?,
    val authors: String?,
    val thumbnailUrl: String,
    val language: String?,
    val description: String?,
    val publisher: String?,
    val publishedDate: String?,
    val pageCount: String?,
    val dimensions: String?,
    val categories: String?,
    val country: String?,
    val retailPrice: String?,
    val previewLink: String?,
    val buyLink: String?,
)
