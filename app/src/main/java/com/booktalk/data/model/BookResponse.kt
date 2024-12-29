package com.booktalk.data.model

data class BookResponse(
    val kind: String,
    val totalItems: Int,
    val items: List<BookDto>
) {
    data class BookDto(
        val id: String,
        val title: String,
        val authors: String,
        val description: String,
        val publishedDate: String,
        val isbn: String,
        val pageCount: Int,
        val categories: String,
        val imageUrl: String,
        val language: String,
        val previewLink: String,
        val averageRating: Double,
        val ratingsCount: Int
    )

    data class VolumeInfo(
        val title: String,
        val authors: List<String>?,
        val publisher: String?,
        val publishedDate: String?,
        val description: String?,
        val industryIdentifiers: List<IndustryIdentifier>?,
        val pageCount: Int?,
        val categories: List<String>?,
        val averageRating: Float?,
        val ratingsCount: Int?,
        val imageLinks: ImageLinks?,
        val language: String?,
        val previewLink: String?
    )

    data class IndustryIdentifier(
        val type: String,
        val identifier: String
    )

    data class ImageLinks(
        val smallThumbnail: String?,
        val thumbnail: String?
    )

    data class SaleInfo(
        val country: String? = null,
        val saleability: String? = null,
        val isEbook: Boolean? = null
    )

    data class AccessInfo(
        val country: String? = null,
        val viewability: String? = null,
        val embeddable: Boolean? = null,
        val publicDomain: Boolean? = null,
        val textToSpeechPermission: String? = null,
        val epub: DownloadInfo? = null,
        val pdf: DownloadInfo? = null,
        val webReaderLink: String? = null,
        val accessViewStatus: String? = null,
        val quoteSharingAllowed: Boolean? = null
    )

    data class DownloadInfo(
        val isAvailable: Boolean? = null,
        val acsTokenLink: String? = null
    )
}
