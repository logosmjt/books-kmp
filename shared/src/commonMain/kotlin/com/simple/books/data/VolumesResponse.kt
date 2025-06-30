package com.simple.books.data

import com.simple.books.db.Book
import com.simple.books.detail.application.BookDetail
import kotlinx.serialization.Serializable

@Serializable
data class VolumesResponse(
    val kind: String? = null,
    val totalItems: Int = 0,
    val items: List<Volume>? = null
)

@Serializable
data class Volume(
    val kind: String? = null,
    val id: String,
    val etag: String? = null,
    val selfLink: String? = null,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo? = null,
    val accessInfo: AccessInfo? = null,
    val searchInfo: SearchInfo? = null
)

@Serializable
data class VolumeInfo(
    val title: String,
    val subtitle: String? = null,
    val authors: List<String>? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val industryIdentifiers: List<IndustryIdentifier>? = null,
    val readingModes: ReadingModes? = null,
    val pageCount: Int? = null,
    val dimensions: Dimensions? = null,
    val printType: String? = null,
    val categories: List<String>? = null,
    val averageRating: Double? = null,
    val ratingsCount: Int? = null,
    val maturityRating: String? = null,
    val allowAnonLogging: Boolean? = null,
    val contentVersion: String? = null,
    val imageLinks: ImageLinks? = null,
    val language: String? = null,
    val previewLink: String? = null,
    val infoLink: String? = null,
    val canonicalVolumeLink: String? = null
)

@Serializable
data class IndustryIdentifier(
    val type: String? = null,
    val identifier: String? = null
)

@Serializable
data class ReadingModes(
    val text: Boolean = false,
    val image: Boolean = false
)

@Serializable
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null,
    val small: String? = null,
    val medium: String? = null,
    val large: String? = null,
    val extraLarge: String? = null
)

@Serializable
data class SaleInfo(
    val country: String? = null,
    val saleability: String? = null,
    val isEbook: Boolean? = null,
    val listPrice: Price? = null,
    val retailPrice: Price? = null,
    val buyLink: String? = null
) {
    fun getRetailPrice(): String =
        "${retailPrice?.amount} ${retailPrice?.currencyCode}"

}

@Serializable
data class Price(
    val amount: Double? = null,
    val currencyCode: String? = null
)

@Serializable
data class Dimensions(
    val height: String? = null,
    val width: String? = null
)

@Serializable
data class AccessInfo(
    val country: String? = null,
    val viewability: String? = null,
    val embeddable: Boolean = false,
    val publicDomain: Boolean = false,
    val textToSpeechPermission: String? = null,
    val epub: FormatAvailability? = null,
    val pdf: FormatAvailability? = null,
    val webReaderLink: String? = null,
    val accessViewStatus: String? = null,
    val quoteSharingAllowed: Boolean = false
)

@Serializable
data class FormatAvailability(
    val isAvailable: Boolean = false,
    val acsTokenLink: String? = null
)

@Serializable
data class SearchInfo(
    val textSnippet: String? = null
)

fun Volume.toBook(query: String): Book {
    return Book(
        id = "${this.id}:$query",
        title = this.volumeInfo.title,
        subtitle = this.volumeInfo.subtitle,
        authors = this.volumeInfo.authors?.joinToString(",").orEmpty(),
        language = this.volumeInfo.language,
        thumbnailUrl = this.volumeInfo.imageLinks?.thumbnail?.replace("http", "https"),
        description = this.volumeInfo.description.orEmpty(),
        publisher = this.volumeInfo.publisher,
        publishedDate = this.volumeInfo.publishedDate,
        query = query
    )
}

fun Volume.toBookDetail(): BookDetail {
    return BookDetail(
        title = this.volumeInfo.title,
        subtitle = this.volumeInfo.subtitle,
        thumbnailUrl = this.volumeInfo.imageLinks?.thumbnail?.replace("http", "https").orEmpty(),
        language = this.volumeInfo.language?.uppercase(),
        description = this.volumeInfo.description,
        publisher = this.volumeInfo.publisher,
        publishedDate = this.volumeInfo.publishedDate?.let { "Published: $it" },
        pageCount = this.volumeInfo.pageCount.toString(),
        dimensions = this.volumeInfo.dimensions.toBookDimensions(),
        categories = this.volumeInfo.getCategories(),
        country = this.accessInfo?.country ,
        previewLink = this.volumeInfo.previewLink,
        authors = this.volumeInfo.getAuthors(),
        retailPrice = this.saleInfo?.getRetailPrice(),
        buyLink = this.saleInfo?.buyLink
    )
}

private fun Dimensions?.toBookDimensions(): String? =
    this?.let { "${it.height} * ${it.width}" }

private fun VolumeInfo?.getAuthors(): String? =
    this?.authors?.let { "By ${it.joinToString(", ")}" }

private fun VolumeInfo?.getCategories(): String? =
    this?.categories?.joinToString("\n")
