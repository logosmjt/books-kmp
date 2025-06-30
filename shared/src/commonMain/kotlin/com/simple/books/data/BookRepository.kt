package com.simple.books.data

import com.simple.books.db.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getBooks(query: String, forceRefresh: Boolean): Flow<List<Book>>

    fun getBookDetail(id: String): Flow<Volume>

    suspend fun getLastQuery(): String?
}
