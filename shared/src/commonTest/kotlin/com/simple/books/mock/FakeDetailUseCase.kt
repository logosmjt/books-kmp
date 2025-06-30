package com.simple.books.mock

import com.simple.books.detail.application.BookDetail
import com.simple.books.detail.application.DetailUseCase
import com.simple.books.fakeError
import com.simple.books.listing.application.Book
import com.simple.books.listing.application.ListingUseCase
import com.simple.books.mock.FakeListingUseCase.Companion.defaultBooks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDetailUseCase(
    private val shouldThrowError: Boolean = false,
) : DetailUseCase {
    override fun invoke(id: String): Flow<BookDetail> = flow {
        if (shouldThrowError) {
            throw fakeError(ERROR_MESSAGE)
        } else {
            emit(defaultBookDetail)
        }
    }

    companion object {
        val defaultBookDetail = BookDetail(
            title = "Effective Kotlin",
            subtitle = "Best Practices",
            authors = "Marcin Moskala",
            thumbnailUrl = "https://example.com/thumb1.jpg",
            language = "en",
            description = "A practical guide to Kotlin programming.",
            publisher = "Leanpub",
            publishedDate = "2020-01-01",
            pageCount = "350",
            dimensions = "6 x 9 inches",
            categories = "Programming, Kotlin",
            country = "US",
            retailPrice = "29.99 USD",
            previewLink = "https://example.com/preview",
            buyLink = "https://example.com/buy"
        )
        const val ERROR_MESSAGE = "Mock getBookDetail error."
    }

}
