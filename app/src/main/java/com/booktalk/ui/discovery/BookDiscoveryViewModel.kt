package com.booktalk.ui.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.repository.BookRepository
import com.booktalk.domain.util.NetworkResult
import com.booktalk.ui.discovery.BookDiscoveryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDiscoveryViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookDiscoveryUiState())
    val uiState: StateFlow<BookDiscoveryUiState> = _uiState.asStateFlow()

    private val _searchResults = MutableStateFlow<PagingData<Book>>(PagingData.empty())
    val searchResults: StateFlow<PagingData<Book>> = _searchResults.asStateFlow()

    private val _categoryBooks = MutableStateFlow<PagingData<Book>>(PagingData.empty())
    val categoryBooks: StateFlow<PagingData<Book>> = _categoryBooks.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.isNotBlank()) {
            searchBooks(query)
        } else {
            _searchResults.value = PagingData.empty()
        }
    }

    private fun searchBooks(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                bookRepository.getSearchBooksPagingFlow(query)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _searchResults.value = pagingData
                        _uiState.update { it.copy(isLoading = false) }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message
                ) }
            }
        }
    }

    fun onCategorySelected(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
        if (category != null) {
            loadCategoryBooks(category)
        } else {
            _categoryBooks.value = PagingData.empty()
        }
    }

    private fun loadCategoryBooks(category: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                bookRepository.getCategoryBooksPagingFlow(category)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _categoryBooks.value = pagingData
                        _uiState.update { it.copy(isLoading = false) }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message
                ) }
            }
        }
    }

    fun clearSearch() {
        _uiState.update { it.copy(searchQuery = "") }
        _searchResults.value = PagingData.empty()
    }

    fun clearCategory() {
        _uiState.update { it.copy(selectedCategory = null) }
        _categoryBooks.value = PagingData.empty()
    }
}
