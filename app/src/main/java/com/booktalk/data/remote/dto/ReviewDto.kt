package com.booktalk.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewDto(
    @Json(name = "id") val id: String,
    @Json(name = "userId") val userId: String,
    @Json(name = "bookId") val bookId: String,
    @Json(name = "rating") val rating: Float?,
    @Json(name = "content") val content: String?,
    @Json(name = "createdAt") val createdAt: Long,
    @Json(name = "updatedAt") val updatedAt: Long,
    @Json(name = "likes") val likes: Int = 0,
    @Json(name = "userDisplayName") val userDisplayName: String? = null,
    @Json(name = "userAvatarUrl") val userAvatarUrl: String? = null
)
