package com.booktalk.data.local.entity

data class BookEntity(
    val id: String,
    val title: String,
    val author: String,
    val description: String?,
    val coverUrl: String?,
    val genres: List<String>?,
    val rating: Float?,
    val pageCount: Int?,
    val publishedDate: String?
)
