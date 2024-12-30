package com.booktalk.data.model

import com.booktalk.data.remote.dto.BookDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookSearchResponse(
    @Json(name = "items") val items: List<BookDto>?,
    @Json(name = "total") val total: Int = 0,
    @Json(name = "page") val page: Int = 1,
    @Json(name = "pageSize") val pageSize: Int = 20,
    @Json(name = "hasMore") val hasMore: Boolean = false
)
