package com.simple.books

import com.simple.books.data.BookDataSource
import com.simple.books.data.BookRepository
import com.simple.books.data.BookRepositoryImpl
import com.simple.books.data.BookService
import com.simple.books.db.BooksDatabase
import com.simple.books.mock.FakeBookFactory
import com.simple.books.mock.FakeBookFactory.Companion.QUERY
import com.simple.books.mock.MockBookService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class BookRepositoryTest {

    private lateinit var database: BooksDatabase
    private lateinit var dataSource: BookDataSource
    private lateinit var bookService: BookService
    private lateinit var repository: BookRepository

    @BeforeTest
    fun setup() {
        database = BooksDatabase(testDbConnection())
        dataSource = BookDataSource(database)
        bookService = MockBookService()
        repository = BookRepositoryImpl(dataSource, bookService)
    }

    @Test
    fun `getBooks when local stores is empty then returns books from service`() = runTest {
        val result = repository.getBooks(QUERY, forceRefresh = false).first()

        assertEquals(2, result.size)
        assertEquals("Effective Kotlin", result.first().title)

        val local = dataSource.getAllBooks(QUERY).first()
        assertEquals(result, local)
    }

    @Test
    fun `getBooks forceRefresh clears and fetches new data`() = runTest {
        // First insert to DB
        dataSource.insertBooks(listOf(FakeBookFactory().createBook()))

        // Then force refresh
        val result = repository.getBooks(QUERY, forceRefresh = true).first()

        assertEquals(2, result.size)
        assertEquals("Effective Kotlin", result.first().title)
    }

    @Test
    fun `getBooks when forceRefresh is false then return existing data`() = runTest {
        // First insert to DB
        dataSource.insertBooks(listOf(FakeBookFactory().createBook()))

        // Then force refresh
        val result = repository.getBooks(QUERY, forceRefresh = false).first()

        assertEquals(1, result.size)
        assertEquals("Effective Kotlin", result.first().title)
    }

    @Test
    fun `getBookDetail returns expected volume`() = runTest {
        val detail = repository.getBookDetail("vol123").first()

        assertEquals("Fake Book Title", detail.volumeInfo.title)
        assertEquals("John Doe", detail.volumeInfo.authors?.first())
    }

    @Test
    fun `getBooks when service fails then throws exception`() = runTest {
        val errorService = MockBookService(shouldThrowError = true)
        val repoWithError = BookRepositoryImpl(dataSource, errorService)

        assertFailsWith<Exception> {
            repoWithError.getBooks(QUERY, forceRefresh = true).first()
        }
    }

    @Test
    fun `getBookDetail when service fails then throws exception`() = runTest {
        val errorService = MockBookService(shouldThrowError = true)
        val repoWithError = BookRepositoryImpl(dataSource, errorService)

        assertFailsWith<Exception> {
            repoWithError.getBookDetail("book-id-123").first()
        }
    }
}
