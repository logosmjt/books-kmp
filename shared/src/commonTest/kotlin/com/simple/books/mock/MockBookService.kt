package com.simple.books.mock

import com.simple.books.data.BookService
import com.simple.books.data.Volume
import com.simple.books.db.Book
import com.simple.books.fakeErrorResponse
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockBookService(
    private val shouldThrowError: Boolean = false,
    private val books: List<Book> = FakeBookFactory().createBookList(),
    private val detail: Volume = FakeBookFactory().createBookDetail()
) : BookService {

    override fun fetchBooks(query: String, maxResults: Int): Flow<List<Book>> = flow {
        if (shouldThrowError) {
            throw ClientRequestException(
                response = fakeErrorResponse(),
                cachedResponseText = "Mock fetchBooks error"
            )
        } else {
            emit(books)
        }
    }

    override fun getBookDetail(id: String): Flow<Volume> = flow {
        if (shouldThrowError) {
            throw ClientRequestException(
                response = fakeErrorResponse(),
                cachedResponseText = "Mock getBookDetail error"
            )
        } else {
            emit(detail)
        }
    }
}
