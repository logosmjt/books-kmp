package com.simple.books.data

import com.simple.books.db.Book
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
class BookRepositoryImpl(
    private val dataSource: BookDataSource,
    private val bookService: BookService
) : BookRepository {
    override fun getBooks(query: String, forceRefresh: Boolean): Flow<List<Book>> = flow {
        if (query != getLastQuery()) {
            insertQuery(query)
        }

        if (forceRefresh) {
            dataSource.clearBooks(query)
            emitAll(fetchBooks(query))
            return@flow
        }

        val localBooks = dataSource.getAllBooks(query).first()
        if (localBooks.isEmpty()) {
            emitAll(fetchBooks(query))
        } else {
            emitAll(dataSource.getAllBooks(query))
        }
    }

    override fun getBookDetail(id: String): Flow<Volume> = flow {
        emitAll(bookService.getBookDetail(id))
    }

    override suspend fun getLastQuery(): String? = dataSource.getLastQuery()?.query

    private suspend fun insertQuery(query: String) = dataSource.insertQuery(query)

    private suspend fun fetchBooks(query: String): Flow<List<Book>> {
        return bookService.fetchBooks(query)
            .flatMapLatest { books ->
                dataSource.insertBooks(books)
                dataSource.getAllBooks(query)
            }
    }

}
