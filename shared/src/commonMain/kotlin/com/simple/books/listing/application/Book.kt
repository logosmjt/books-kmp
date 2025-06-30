package com.simple.books.listing.application

data class Book(
    val id: String,
    val title: String?,
    val subtitle: String?,
    val authors: List<String>,
    val thumbnailUrl: String?,
    val language: String?,
    val description: String?,
    val publisher: String?,
    val publishedDate: String?,
)

fun com.simple.books.db.Book.toAppModel(): Book {
    return Book(
        id = this.id.split(":")[0],
        title = this.title,
        subtitle = this.subtitle,
        authors = this.authors?.split(",") ?: emptyList(),
        language = this.language,
        thumbnailUrl = this.thumbnailUrl,
        description = this.description,
        publisher = this.publisher,
        publishedDate = this.publishedDate
    )
}
