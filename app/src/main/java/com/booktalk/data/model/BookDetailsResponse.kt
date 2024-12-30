package com.booktalk.data.model

import com.booktalk.data.remote.dto.BookDto
import com.booktalk.data.remote.dto.ReviewDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookDetailsResponse(
    @Json(name = "book") val book: BookDto,
    @Json(name = "averageRating") val averageRating: Float? = null,
    @Json(name = "ratingsCount") val ratingsCount: Int? = null,
    @Json(name = "reviews") val reviews: List<ReviewDto>? = null,
    @Json(name = "relatedBooks") val relatedBooks: List<BookDto>? = null
)
