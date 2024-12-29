package com.booktalk.ui.readinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.repository.BookRepository
import com.booktalk.domain.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReadingListUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ReadingListViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReadingListUiState())
    val uiState: StateFlow<ReadingListUiState> = _uiState

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                bookRepository.getReadingList().collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    books = result.data,
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }
                        is NetworkResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.message ?: "Failed to load reading list"
                                )
                            }
                        }
                        is NetworkResult.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }
}
