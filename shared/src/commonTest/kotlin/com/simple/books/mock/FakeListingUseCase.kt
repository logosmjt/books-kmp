package com.simple.books.mock

import com.simple.books.fakeError
import com.simple.books.listing.application.Book
import com.simple.books.listing.application.ListingUseCase
import com.simple.books.mock.FakeBookFactory.Companion.QUERY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeListingUseCase(
    private val shouldThrowError: Boolean = false,
) : ListingUseCase {
    override fun invoke(
        query: String,
        forceRefresh: Boolean
    ): Flow<List<Book>> = flow {
        if (shouldThrowError) {
            throw fakeError(ERROR_MESSAGE)
        } else if (query == QUERY) {
            emit(defaultBooks)
        } else {
            emit(emptyList())
        }
    }

    companion object {
        val defaultBooks = listOf(
            Book(
                id = "1",
                title = "Effective Kotlin",
                subtitle = "Best practices",
                authors = listOf("Marcin Moskala"),
                thumbnailUrl = "https://example.com/thumb1.jpg",
                language = "en",
                description = "A guide to writing idiomatic Kotlin",
                publisher = "Leanpub",
                publishedDate = "2020-01-01"
            ),
            Book(
                id = "2",
                title = "Kotlin in Action",
                subtitle = null,
                authors = listOf("Dmitry Jemerov", "Svetlana Isakova"),
                thumbnailUrl = "https://example.com/thumb2.jpg",
                language = "en",
                description = "Deep dive into Kotlin",
                publisher = "Manning",
                publishedDate = "2017-03-01"
            )
        )

        const val ERROR_MESSAGE = "Mock getBooks error."
    }

}
