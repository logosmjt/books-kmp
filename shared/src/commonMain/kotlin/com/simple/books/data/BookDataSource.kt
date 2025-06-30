package com.simple.books.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.simple.books.db.Book
import com.simple.books.db.BooksDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock.System

class BookDataSource(private val database: BooksDatabase?) {

    fun getAllBooks(query: String): Flow<List<Book>> =
        database?.booksQueries?.selectByQuery(query)?.asFlow()
            ?.mapToList(Dispatchers.IO)
            ?: flowOf(emptyList())

    suspend fun insertBooks(books: List<Book>) {
        database?.booksQueries?.transaction {
            books.forEach { book ->
                insertBook(book)
            }
        }
    }

    suspend fun getLastQuery() = database?.booksQueries?.selectLastQuery()?.executeAsOneOrNull()

    suspend fun insertQuery(query: String) =
        database?.booksQueries?.insertSearch(query, System.now().toString())

    suspend fun clearBooks(query: String) = database?.booksQueries?.deleteAllByQuery(query)

    private fun insertBook(book: Book) {
        database?.booksQueries?.insertBook(
            book.id,
            book.title,
            book.subtitle,
            book.authors,
            book.thumbnailUrl,
            book.language,
            book.description,
            book.publisher,
            book.publishedDate,
            book.query
        )
    }
}
