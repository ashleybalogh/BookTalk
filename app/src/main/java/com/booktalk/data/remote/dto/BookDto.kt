package com.booktalk.data.remote.dto

data class BookDto(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val isbn: String,
    val coverUrl: String,
    val pageCount: Int,
    val publishedDate: String,
    val publisher: String,
    val categories: List<String>?,
    val averageRating: Double?,
    val ratingsCount: Int?,
    val language: String?
)
