package com.simple.books.mock

import com.simple.books.data.BookRepository
import com.simple.books.data.Volume
import com.simple.books.db.Book
import com.simple.books.fakeError
import com.simple.books.mock.FakeBookFactory.Companion.QUERY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBookRepository(
    private val shouldThrowError: Boolean = false,
    private val books: List<Book> = FakeBookFactory().createBookList(),
    private val detail: Volume = FakeBookFactory().createBookDetail()
) : BookRepository {
    override fun getBooks(
        query: String,
        forceRefresh: Boolean
    ): Flow<List<Book>> = flow {
        if (shouldThrowError) {
            throw fakeError("Mock getBooks error.")
        } else {
            emit(books)
        }
    }

    override fun getBookDetail(id: String): Flow<Volume> = flow {
        if (shouldThrowError) {
            throw fakeError("Mock getBookDetail error.")
        } else {
            emit(detail)
        }
    }

    override suspend fun getLastQuery(): String? {
        if (shouldThrowError) {
            throw fakeError("Mock getLastQuery error.")
        } else {
            return QUERY
        }
    }

}
