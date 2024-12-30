package com.booktalk.data.model

data class BookDto(
    val id: String,
    val title: String,
    val author: String,
    val description: String?,
    val coverUrl: String?,
    val genres: List<String>? = null,
    val rating: Float? = null,
    val pageCount: Int? = null,
    val publishedDate: String? = null
)
