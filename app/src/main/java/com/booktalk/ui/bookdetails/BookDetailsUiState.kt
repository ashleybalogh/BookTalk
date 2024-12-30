package com.booktalk.ui.bookdetails

import com.booktalk.domain.model.book.Book
import com.booktalk.domain.model.review.Review

data class BookDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val book: Book? = null,
    val reviews: List<Review> = emptyList(),
    val relatedBooks: List<Book> = emptyList(),
    val userRating: Float? = null,
    val userReview: String? = null,
    val isInReadingList: Boolean = false,
    val readingProgress: Int = 0,
    val totalPages: Int = 0
)
