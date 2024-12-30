package com.booktalk.ui.readinglist

import com.booktalk.domain.model.book.Book
import com.booktalk.domain.model.book.ReadingStatus

data class ReadingListUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val books: List<Book> = emptyList(),
    val selectedStatus: ReadingStatus? = null,
    val filterOptions: List<ReadingStatus> = ReadingStatus.values().toList()
)
