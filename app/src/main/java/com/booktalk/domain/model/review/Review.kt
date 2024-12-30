package com.booktalk.domain.model.review

data class Review(
    val id: String? = null,
    val userId: String? = null,
    val bookId: String? = null,
    val rating: Float? = null,
    val content: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
    val likes: Int = 0,
    val userDisplayName: String? = null,
    val userAvatarUrl: String? = null
)
