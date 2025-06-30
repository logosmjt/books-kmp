package com.simple.books

import com.simple.books.data.BookDataSource
import com.simple.books.db.Book
import com.simple.books.db.BooksDatabase
import kotlin.test.BeforeTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class BookDataSourceTest {

    private lateinit var database: BooksDatabase
    private lateinit var dataSource: BookDataSource

    @BeforeTest
    fun setup() {
        val driver = testDbConnection()
        database = BooksDatabase(driver)
        dataSource = BookDataSource(database)
    }

    @Test
    fun `getAllBooks returns empty list when no books inserted`() = runTest {
        val books = dataSource.getAllBooks("kotlin").first()
        assertTrue(books.isEmpty(), "Expected no books, but found some")
    }

    @Test
    fun `insertBooks then getAllBooks returns inserted books`() = runTest {
        val book = sampleBook(query = "kotlin")
        dataSource.insertBooks(listOf(book))

        val books = dataSource.getAllBooks("kotlin").first()

        assertEquals(1, books.size)
        assertEquals(book.id, books.first().id)
        assertEquals(book.title, books.first().title)
    }

    @Test
    fun `insertQuery then getLastQuery returns inserted query`() = runTest {
        val query = "multiplatform"
        dataSource.insertQuery(query)

        val lastQuery = dataSource.getLastQuery()?.query

        assertNotNull(lastQuery)
        assertEquals(query, lastQuery)
    }

    @Test
    fun `clearBooks removes only matching query books`() = runTest {
        val kotlinBook = sampleBook(id = "1", query = "kotlin")
        val composeBook = sampleBook(id = "2", query = "compose")
        dataSource.insertBooks(listOf(kotlinBook, composeBook))

        dataSource.clearBooks("kotlin")

        val kotlinBooks = dataSource.getAllBooks("kotlin").first()
        val composeBooks = dataSource.getAllBooks("compose").first()

        assertTrue(kotlinBooks.isEmpty())
        assertEquals(1, composeBooks.size)
        assertEquals("compose", composeBooks.first().query)
    }

    private fun sampleBook(
        id: String = "book-1",
        query: String = "kotlin"
    ) = Book(
        id = id,
        title = "Kotlin in Action",
        subtitle = "A comprehensive guide",
        authors = "JetBrains",
        thumbnailUrl = "https://example.com/image.png",
        language = "en",
        description = "A book about Kotlin.",
        publisher = "Manning",
        publishedDate = "2020-01-01",
        query = query
    )
}
