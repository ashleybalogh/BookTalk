package com.booktalk.domain.model.book

data class Book(
    val id: String,
    val title: String,
    val authors: List<String>,
    val description: String,
    val imageUrl: String,
    val pageCount: Int,
    val publishedDate: String,
    val publisher: String,
    val categories: List<String>,
    val language: String,
    val isbn: String,
    val averageRating: Float? = null,
    val ratingsCount: Int = 0,
    val previewLink: String? = null,
    val infoLink: String? = null,
    val buyLink: String? = null,
    val status: ReadingStatus = ReadingStatus.WANT_TO_READ,
    val currentPage: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
