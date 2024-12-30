package com.booktalk.ui.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booktalk.data.remote.util.NetworkResult
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookDiscoveryUiState(
    val searchQuery: String = "",
    val selectedGenre: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val searchResults: List<Book> = emptyList(),
    val recommendedBooks: List<Book> = emptyList(),
    val popularBooks: List<Book> = emptyList(),
    val genreBooks: List<Book> = emptyList()
)

@HiltViewModel
class BookDiscoveryViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookDiscoveryUiState())
    val uiState: StateFlow<BookDiscoveryUiState> = _uiState

    init {
        loadPopularBooks()
    }

    private fun loadPopularBooks() {
        viewModelScope.launch {
            bookRepository.getAllBooks().collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                popularBooks = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                error = result.message,
                                isLoading = false
                            )
                        }
                    }
                    is NetworkResult.Loading -> {
                        _uiState.update { currentState ->
                            currentState.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.isNotEmpty()) {
            searchBooks(query)
        } else {
            clearSearch()
        }
    }

    private fun searchBooks(query: String) {
        viewModelScope.launch {
            bookRepository.searchBooks(query).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                searchResults = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                error = result.message,
                                isLoading = false
                            )
                        }
                    }
                    is NetworkResult.Loading -> {
                        _uiState.update { currentState ->
                            currentState.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }

    fun onGenreSelected(genre: String) {
        _uiState.update { it.copy(selectedGenre = genre) }
        loadGenreBooks(genre)
    }

    private fun loadGenreBooks(genre: String) {
        viewModelScope.launch {
            bookRepository.getBooksByGenre(genre).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                genreBooks = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                error = result.message,
                                isLoading = false
                            )
                        }
                    }
                    is NetworkResult.Loading -> {
                        _uiState.update { currentState ->
                            currentState.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }

    fun clearGenre() {
        _uiState.update { currentState ->
            currentState.copy(
                selectedGenre = null,
                genreBooks = emptyList()
            )
        }
    }

    fun clearSearch() {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = "",
                searchResults = emptyList()
            )
        }
    }
}
