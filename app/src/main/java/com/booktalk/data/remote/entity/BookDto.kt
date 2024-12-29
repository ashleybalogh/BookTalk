package com.booktalk.data.remote.entity

data class BookDto(
    val id: String,
    val title: String,
    val authors: List<String>? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val pageCount: Int? = null,
    val publishedDate: String? = null,
    val publisher: String? = null,
    val categories: List<String>? = null,
    val language: String? = null,
    val previewLink: String? = null,
    val infoLink: String? = null,
    val maturityRating: String? = null,
    val averageRating: Float? = null,
    val ratingsCount: Int? = null,
    val reviewCount: Int? = null,
    val popularity: Float = 0f,
    val keywords: List<String>? = null,
    val similarBooks: List<String>? = null
)
