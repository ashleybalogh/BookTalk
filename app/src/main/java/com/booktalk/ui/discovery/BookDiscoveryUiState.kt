package com.booktalk.ui.discovery

import androidx.paging.PagingData
import com.booktalk.domain.model.book.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class BookDiscoveryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val popularBooks: Flow<PagingData<Book>> = emptyFlow(),
    val searchResults: Flow<PagingData<Book>> = emptyFlow(),
    val categoryBooks: Flow<PagingData<Book>> = emptyFlow()
)
