package com.booktalk.data.mapper

import com.booktalk.data.local.entity.BookEntity
import com.booktalk.data.remote.model.BookDto
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.model.book.ReadingStatus

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        title = title,
        authors = authors,
        description = description,
        imageUrl = imageUrl,
        pageCount = pageCount,
        publishedDate = publishedDate,
        publisher = publisher,
        categories = categories,
        language = language,
        previewLink = previewLink,
        infoLink = infoLink,
        maturityRating = maturityRating,
        averageRating = averageRating,
        ratingsCount = ratingsCount,
        reviewCount = reviewCount,
        popularity = popularity,
        keywords = keywords,
        similarBooks = similarBooks,
        status = status
    )
}

fun Book.toEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = title,
        authors = authors,
        description = description,
        imageUrl = imageUrl,
        pageCount = pageCount,
        publishedDate = publishedDate,
        publisher = publisher,
        categories = categories,
        language = language,
        previewLink = previewLink,
        infoLink = infoLink,
        maturityRating = maturityRating,
        averageRating = averageRating,
        ratingsCount = ratingsCount,
        reviewCount = reviewCount,
        popularity = popularity,
        keywords = keywords,
        similarBooks = similarBooks,
        status = status
    )
}

fun BookDto.toBook(): Book {
    return Book(
        id = id,
        title = volumeInfo.title,
        authors = volumeInfo.authors ?: emptyList(),
        description = volumeInfo.description,
        imageUrl = volumeInfo.imageLinks?.thumbnail,
        pageCount = volumeInfo.pageCount,
        publishedDate = volumeInfo.publishedDate,
        publisher = volumeInfo.publisher,
        categories = volumeInfo.categories ?: emptyList(),
        language = volumeInfo.language,
        previewLink = volumeInfo.previewLink,
        infoLink = volumeInfo.infoLink,
        maturityRating = volumeInfo.maturityRating,
        averageRating = volumeInfo.averageRating ?: 0f,
        ratingsCount = volumeInfo.ratingsCount ?: 0,
        reviewCount = 0,
        popularity = 0f,
        keywords = emptyList(),
        similarBooks = emptyList(),
        status = ReadingStatus.NONE
    )
}
