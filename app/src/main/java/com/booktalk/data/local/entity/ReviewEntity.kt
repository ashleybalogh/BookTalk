package com.booktalk.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "reviews",
    primaryKeys = ["id"],
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["bookId"]),
        Index(value = ["userId"])
    ]
)
data class ReviewEntity(
    val id: String,
    val userId: String,
    val bookId: String,
    val rating: Float? = null,
    val content: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val likes: Int = 0,
    val userDisplayName: String? = null,
    val userAvatarUrl: String? = null
)
