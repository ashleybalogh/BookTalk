package com.booktalk.domain.model.book

data class Review(
    val id: String,
    val userId: String,
    val bookId: String,
    val rating: Float,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
    val userName: String? = null,
    val userAvatar: String? = null
)
