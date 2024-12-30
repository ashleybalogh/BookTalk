package com.booktalk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.booktalk.data.local.converter.Converters
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.model.book.ReadingStatus

@Entity(tableName = "books")
@TypeConverters(Converters::class)
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val authors: String,  // Stored as comma-separated string
    val description: String,
    val coverUrl: String,
    val pageCount: Int,
    val publishedDate: String,
    val publisher: String,
    val categories: String,  // Stored as comma-separated string
    val language: String,
    val isbn: String,
    val averageRating: Float? = null,
    val ratingsCount: Int = 0,
    val previewLink: String? = null,
    val infoLink: String? = null,
    val buyLink: String? = null,
    val status: ReadingStatus? = null,
    val currentPage: Int? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun fromDomain(book: Book) = BookEntity(
            id = book.id,
            title = book.title,
            author = book.authors.firstOrNull() ?: "",
            authors = book.authors.joinToString(","),
            description = book.description,
            coverUrl = book.imageUrl,
            pageCount = book.pageCount,
            publishedDate = book.publishedDate,
            publisher = book.publisher,
            categories = book.categories.joinToString(","),
            language = book.language,
            isbn = book.isbn,
            averageRating = book.averageRating,
            ratingsCount = book.ratingsCount,
            previewLink = book.previewLink,
            infoLink = book.infoLink,
            buyLink = book.buyLink,
            status = book.status,
            currentPage = book.currentPage,
            createdAt = book.createdAt,
            updatedAt = book.updatedAt
        )
    }

    fun toDomain() = Book(
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
