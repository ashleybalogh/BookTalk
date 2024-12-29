package com.booktalk.data.model

data class BooksResponse(
    val kind: String,
    val totalItems: Int,
    val items: List<BookDto>
)
