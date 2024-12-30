package com.booktalk.data.mapper

import com.booktalk.data.local.entity.BookEntity
import com.booktalk.data.remote.dto.BookDto
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.model.book.ReadingStatus

fun BookDto.toBook(): Book {
    return Book(
        id = id,
        title = title,
        authors = authors ?: listOf(author),
        description = description,
        imageUrl = coverUrl,
        pageCount = pageCount,
        publishedDate = publishedDate,
        publisher = publisher,
        categories = categories ?: emptyList(),
        language = language ?: "en",  // Provide a default value when language is null
        isbn = isbn,
        averageRating = averageRating?.toFloat(),
        ratingsCount = ratingsCount,
        previewLink = previewLink,
        infoLink = infoLink,
        buyLink = buyLink,
        status = ReadingStatus.WANT_TO_READ,
        currentPage = 0,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        title = title,
        authors = authors.split(",").map { it.trim() },
        description = description,
        imageUrl = coverUrl,
        pageCount = pageCount,
        publishedDate = publishedDate,
        publisher = publisher,
        categories = categories.split(",").map { it.trim() },
        language = language,
        isbn = isbn,
        averageRating = averageRating,
        ratingsCount = ratingsCount,
        previewLink = previewLink,
        infoLink = infoLink,
        buyLink = buyLink,
        status = status ?: ReadingStatus.WANT_TO_READ,
        currentPage = currentPage ?: 0,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Book.toBookEntity(): BookEntity {
    return BookEntity(
        id = id ?: "",
        title = title ?: "",
        author = authors?.firstOrNull() ?: "",
        authors = authors?.joinToString(",") ?: "",
        description = description ?: "",
        coverUrl = imageUrl ?: "",
        pageCount = pageCount ?: 0,
        publishedDate = publishedDate ?: "",
        publisher = publisher ?: "",
        categories = categories?.joinToString(",") ?: "",
        language = language ?: "",
        isbn = isbn ?: "",
        averageRating = averageRating,
        ratingsCount = ratingsCount ?: 0,
        previewLink = previewLink,
        infoLink = infoLink,
        buyLink = buyLink,
        status = status ?: ReadingStatus.WANT_TO_READ,
        currentPage = currentPage ?: 0,
        createdAt = createdAt ?: System.currentTimeMillis(),
        updatedAt = updatedAt ?: System.currentTimeMillis()
    )
}
