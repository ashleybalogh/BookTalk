package com.booktalk.data.remote.model

import com.google.gson.annotations.SerializedName

data class BookDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("volumeInfo")
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    @SerializedName("title")
    val title: String,
    @SerializedName("authors")
    val authors: List<String>?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("imageLinks")
    val imageLinks: ImageLinks?,
    @SerializedName("industryIdentifiers")
    val industryIdentifiers: List<IndustryIdentifier>?,
    @SerializedName("pageCount")
    val pageCount: Int?,
    @SerializedName("publishedDate")
    val publishedDate: String?,
    @SerializedName("publisher")
    val publisher: String?,
    @SerializedName("categories")
    val categories: List<String>?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("averageRating")
    val averageRating: Float?,
    @SerializedName("ratingsCount")
    val ratingsCount: Int?
)

data class ImageLinks(
    @SerializedName("smallThumbnail")
    val smallThumbnail: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("small")
    val small: String?,
    @SerializedName("medium")
    val medium: String?,
    @SerializedName("large")
    val large: String?
)

data class IndustryIdentifier(
    @SerializedName("type")
    val type: String,
    @SerializedName("identifier")
    val identifier: String
)
