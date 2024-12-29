package com.booktalk.data.model

data class BookDto(
    val id: String,
    val title: String,
    val author: String,
    val description: String?,
    val coverUrl: String?,
    val isbn: String?,
    val pageCount: Int?,
    val publishedDate: String?,
    val publisher: String?,
    val categories: List<String>?,
    val language: String?,
    val averageRating: Float?,
    val ratingsCount: Int?
)
