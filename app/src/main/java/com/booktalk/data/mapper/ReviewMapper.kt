package com.booktalk.data.mapper

import com.booktalk.data.local.entity.ReviewEntity
import com.booktalk.data.remote.dto.ReviewDto
import com.booktalk.domain.model.review.Review

fun ReviewDto.toReview(): Review {
    return Review(
        id = id,
        userId = userId,
        bookId = bookId,
        rating = rating,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
        likes = likes,
        userDisplayName = userDisplayName,
        userAvatarUrl = userAvatarUrl
    )
}

fun Review.toReviewEntity(): ReviewEntity {
    return ReviewEntity(
        id = id ?: "",
        userId = userId ?: "",
        bookId = bookId ?: "",
        rating = rating,
        content = content,
        createdAt = createdAt ?: System.currentTimeMillis(),
        updatedAt = updatedAt ?: System.currentTimeMillis(),
        likes = likes,
        userDisplayName = userDisplayName,
        userAvatarUrl = userAvatarUrl
    )
}

fun ReviewEntity.toReview(): Review {
    return Review(
        id = id,
        userId = userId,
        bookId = bookId,
        rating = rating,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
        likes = likes,
        userDisplayName = userDisplayName,
        userAvatarUrl = userAvatarUrl
    )
}
