package com.booktalk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.booktalk.data.local.converter.StringListConverter
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.model.book.ReadingStatus

@Entity(tableName = "books")
@TypeConverters(StringListConverter::class)
data class BookEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val authors: List<String> = emptyList(),
    val description: String? = null,
    val imageUrl: String? = null,
    val pageCount: Int? = null,
    val publishedDate: String? = null,
    val publisher: String? = null,
    val categories: List<String> = emptyList(),
    val language: String? = null,
    val previewLink: String? = null,
    val infoLink: String? = null,
    val maturityRating: String? = null,
    val averageRating: Float = 0f,
    val ratingsCount: Int = 0,
    val reviewCount: Int = 0,
    val popularity: Float = 0f,
    val keywords: List<String> = emptyList(),
    val similarBooks: List<String> = emptyList(),
    val status: ReadingStatus = ReadingStatus.NONE,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun fromDomain(book: Book) = BookEntity(
            id = book.id,
            title = book.title,
            authors = book.authors ?: emptyList(),
            description = book.description,
            imageUrl = book.imageUrl,
            pageCount = book.pageCount,
            publishedDate = book.publishedDate,
            publisher = book.publisher,
            categories = book.categories ?: emptyList(),
            language = book.language,
            previewLink = book.previewLink,
            infoLink = book.infoLink,
            maturityRating = book.maturityRating,
            averageRating = book.averageRating ?: 0f,
            ratingsCount = book.ratingsCount ?: 0,
            reviewCount = book.reviewCount ?: 0,
            popularity = book.popularity,
            keywords = book.keywords,
            similarBooks = book.similarBooks,
            status = book.status
        )
    }

    fun toDomain() = Book(
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
