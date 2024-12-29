package com.booktalk.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val authors: String,
    val description: String,
    val publishedDate: String,
    val isbn: String,
    val pageCount: Int,
    val categories: String,
    val imageUrl: String,
    val language: String,
    val previewLink: String,
    val averageRating: Double,
    val ratingsCount: Int,
    val updatedAt: Long = System.currentTimeMillis()
)
