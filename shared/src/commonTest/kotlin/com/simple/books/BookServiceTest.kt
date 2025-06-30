package com.simple.books

import com.simple.books.data.BookService
import com.simple.books.data.BookServiceImpl
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.*

class BookServiceTest {

    private lateinit var service: BookService

    @Test
    fun `fetchBooks returns parsed book list`() = runTest {
        val mockEngine = MockEngine { request ->
            assertTrue(request.url.toString().startsWith("https://www.googleapis.com/books/v1/volumes"))
            respond(
                content = MOCK_VOLUMES_RESPONSE,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        service = BookServiceImpl(client)

        val books = service.fetchBooks("kotlin").first()

        assertEquals(1, books.size)
        assertEquals("Effective Kotlin", books[0].title)
        assertEquals("Marcin Moskala", books[0].authors)
    }

    @Test
    fun `getBookDetail returns single volume`() = runTest {
        val mockEngine = MockEngine { request ->
            assertTrue(request.url.toString().contains("volumes/test-id"))
            respond(
                content = MOCK_VOLUME_DETAIL_RESPONSE,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        service = BookServiceImpl(client)

        val volume = service.getBookDetail("test-id").first()

        assertEquals("Effective Kotlin", volume.volumeInfo.title)
        assertEquals("Marcin Moskala", volume.volumeInfo.authors?.first())
    }

    companion object {
        private const val MOCK_VOLUMES_RESPONSE = """
            {
              "items": [
                {
                  "id": "1",
                  "volumeInfo": {
                    "title": "Effective Kotlin",
                    "authors": ["Marcin Moskala"]
                  }
                }
              ]
            }
        """

        private const val MOCK_VOLUME_DETAIL_RESPONSE = """
            {
              "id": "1",
              "volumeInfo": {
                "title": "Effective Kotlin",
                "authors": ["Marcin Moskala"]
              }
            }
        """
    }
}
