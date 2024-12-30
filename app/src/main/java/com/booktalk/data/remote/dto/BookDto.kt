package com.booktalk.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookDto(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "author") val author: String,
    @Json(name = "authors") val authors: List<String>?,
    @Json(name = "description") val description: String,
    @Json(name = "isbn") val isbn: String,
    @Json(name = "coverUrl") val coverUrl: String,
    @Json(name = "pageCount") val pageCount: Int,
    @Json(name = "publishedDate") val publishedDate: String,
    @Json(name = "publisher") val publisher: String,
    @Json(name = "categories") val categories: List<String>?,
    @Json(name = "language") val language: String?,
    @Json(name = "averageRating") val averageRating: Float? = null,
    @Json(name = "ratingsCount") val ratingsCount: Int = 0,
    @Json(name = "previewLink") val previewLink: String? = null,
    @Json(name = "infoLink") val infoLink: String? = null,
    @Json(name = "buyLink") val buyLink: String? = null
)
