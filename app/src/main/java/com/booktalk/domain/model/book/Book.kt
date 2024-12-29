package com.booktalk.domain.model.book

data class Book(
    val id: String,
    val title: String,
    val authors: List<String> = emptyList(),
    val description: String? = null,
    val imageUrl: String? = null,
    val pageCount: Int? = null,
    val publishedDate: String? = null,
    val publisher: String? = null,
    val categories: List<String> = emptyList(),
    val language: String? = null,
    val previewLink: String? = null,
    val infoLink: String? = null,
    val maturityRating: String? = null,
    val averageRating: Float = 0f,
    val ratingsCount: Int = 0,
    val reviewCount: Int = 0,
    val popularity: Float = 0f,
    val keywords: List<String> = emptyList(),
    val similarBooks: List<String> = emptyList(),
    val status: ReadingStatus = ReadingStatus.NONE
)
