package com.simple.books.data

import com.simple.books.db.Book
import kotlinx.coroutines.flow.Flow

interface BookService {
    fun getBookDetail(id: String): Flow<Volume>

    fun fetchBooks(query: String, maxResults: Int = 10): Flow<List<Book>>
}
