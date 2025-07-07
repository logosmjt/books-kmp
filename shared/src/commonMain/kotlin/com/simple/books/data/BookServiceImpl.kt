package com.simple.books.data

import com.simple.books.db.Book
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BookServiceImpl(private val httpClient: HttpClient): BookService {

    override fun getBookDetail(id: String): Flow<Volume> = flow {
        emit(httpClient.get("https://www.googleapis.com/books/v1/volumes/$id").body())
    }


    override fun fetchBooks(query: String, maxResults: Int): Flow<List<Book>> = flow {
        val response: VolumesResponse =
            httpClient.get("https://www.googleapis.com/books/v1/volumes") {
                parameter("q", query)
                parameter("maxResults", maxResults)
            }.body()

        emit(response.items?.map { it.toBook(query) } ?: emptyList())
    }

}
