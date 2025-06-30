package com.simple.books.mock

import com.simple.books.data.AccessInfo
import com.simple.books.data.Dimensions
import com.simple.books.data.FormatAvailability
import com.simple.books.data.ImageLinks
import com.simple.books.data.IndustryIdentifier
import com.simple.books.data.Price
import com.simple.books.data.ReadingModes
import com.simple.books.data.SaleInfo
import com.simple.books.data.SearchInfo
import com.simple.books.data.Volume
import com.simple.books.data.VolumeInfo
import com.simple.books.db.Book

class FakeBookFactory {
    fun createBook(query: String = QUERY) =  Book(
        id = "book1",
        title = "Effective Kotlin",
        subtitle = "Best practices",
        authors = "Marcin Moskala",
        thumbnailUrl = "https://example.com/thumb.jpg",
        language = "en",
        description = "A book about best Kotlin practices.",
        publisher = "Kotlin Press",
        publishedDate = "2021-06-15",
        query = query
    )

    fun createBookList(query: String = QUERY): List<Book> {
        return listOf(
            createBook(),
            Book(
                id = "book2",
                title = "Kotlin in Action",
                subtitle = null,
                authors = "Dmitry Jemerov, Svetlana Isakova",
                thumbnailUrl = "https://example.com/kia.jpg",
                language = "en",
                description = "A hands-on guide to Kotlin.",
                publisher = "Manning",
                publishedDate = "2017-02-19",
                query = query
            )
        )
    }

    fun createBookDetail(id: String = "vol123"): Volume {
        return Volume(
            kind = "books#volume",
            id = id,
            etag = "etag123",
            selfLink = "https://www.googleapis.com/books/v1/volumes/$id",
            volumeInfo = VolumeInfo(
                title = "Fake Book Title",
                subtitle = "Fake Subtitle",
                authors = listOf("John Doe", "Jane Smith"),
                publisher = "Fake Publisher",
                publishedDate = "2023-01-01",
                description = "A description of the fake book.",
                industryIdentifiers = listOf(
                    IndustryIdentifier("ISBN_13", "9781234567897"),
                    IndustryIdentifier("ISBN_10", "123456789X")
                ),
                readingModes = ReadingModes(text = true, image = false),
                pageCount = 321,
                dimensions = Dimensions(height = "20cm", width = "13cm"),
                printType = "BOOK",
                categories = listOf("Fiction", "Programming"),
                averageRating = 4.5,
                ratingsCount = 99,
                maturityRating = "NOT_MATURE",
                allowAnonLogging = true,
                contentVersion = "preview-1.0.0",
                imageLinks = ImageLinks(
                    smallThumbnail = "https://example.com/smallThumb.jpg",
                    thumbnail = "https://example.com/thumb.jpg"
                ),
                language = "en",
                previewLink = "https://example.com/preview",
                infoLink = "https://example.com/info",
                canonicalVolumeLink = "https://example.com/book"
            ),
            saleInfo = SaleInfo(
                country = "US",
                saleability = "FOR_SALE",
                isEbook = true,
                listPrice = Price(amount = 19.99, currencyCode = "USD"),
                retailPrice = Price(amount = 15.99, currencyCode = "USD"),
                buyLink = "https://example.com/buy"
            ),
            accessInfo = AccessInfo(
                country = "US",
                viewability = "PARTIAL",
                embeddable = true,
                publicDomain = false,
                textToSpeechPermission = "ALLOWED",
                epub = FormatAvailability(isAvailable = true),
                pdf = FormatAvailability(isAvailable = false),
                webReaderLink = "https://example.com/webreader",
                accessViewStatus = "SAMPLE",
                quoteSharingAllowed = true
            ),
            searchInfo = SearchInfo(textSnippet = "A great fake book about Kotlin.")
        )
    }

    companion object {
        const val QUERY = "kotlin"
    }
}
