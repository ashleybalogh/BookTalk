package com.booktalk.ui.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.repository.BookRepository
import com.booktalk.domain.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookDiscoveryUiState(
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val searchResults: List<Book> = emptyList(),
    val popularBooks: List<Book> = emptyList(),
    val categoryBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class BookDiscoveryViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookDiscoveryUiState())
    val uiState: StateFlow<BookDiscoveryUiState> = _uiState.asStateFlow()

    init {
        loadPopularBooks()
    }

    private fun loadPopularBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            bookRepository.getBooksByCategory("popular").collect { result: NetworkResult<List<Book>> ->
                when (result) {
                    is NetworkResult.Success -> {
                        _uiState.update { 
                            it.copy(
                                popularBooks = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Failed to load popular books"
                            )
                        }
                    }
                    is NetworkResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchBooks(query)
    }

    private fun searchBooks(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList()) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            bookRepository.searchBooks(query).collect { result: NetworkResult<List<Book>> ->
                when (result) {
                    is NetworkResult.Success -> {
                        _uiState.update { 
                            it.copy(
                                searchResults = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Failed to search books"
                            )
                        }
                    }
                    is NetworkResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                    }
                }
            }
        }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        selectCategory(category)
    }

    private fun selectCategory(category: String?) {
        if (category == null) {
            _uiState.update { it.copy(categoryBooks = emptyList()) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            bookRepository.getBooksByCategory(category).collect { result: NetworkResult<List<Book>> ->
                when (result) {
                    is NetworkResult.Success -> {
                        _uiState.update { 
                            it.copy(
                                categoryBooks = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Failed to load books for category"
                            )
                        }
                    }
                    is NetworkResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                    }
                }
            }
        }
    }

    fun clearSearch() {
        _uiState.update { it.copy(
            searchQuery = "",
            searchResults = emptyList()
        )}
        loadPopularBooks()
    }

    fun clearCategory() {
        _uiState.update { it.copy(
            selectedCategory = null,
            categoryBooks = emptyList()
        )}
        loadPopularBooks()
    }
}
