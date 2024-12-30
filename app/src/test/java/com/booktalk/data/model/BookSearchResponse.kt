package com.booktalk.data.model

data class BookSearchResponse(
    val books: List<BookDto>,
    val totalItems: Int,
    val nextPage: Int?
)
