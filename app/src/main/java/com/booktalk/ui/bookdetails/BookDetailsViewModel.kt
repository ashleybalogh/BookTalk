package com.booktalk.ui.bookdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.repository.BookRepository
import com.booktalk.domain.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookDetailsUiState(
    val book: Book? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookDetailsUiState())
    val uiState: StateFlow<BookDetailsUiState> = _uiState.asStateFlow()

    fun loadBook(bookId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            bookRepository.getBookById(bookId).collect { result: com.booktalk.domain.util.NetworkResult<Book> ->
                when (result) {
                    is com.booktalk.domain.util.NetworkResult.Success -> {
                        _uiState.update { 
                            it.copy(
                                book = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is com.booktalk.domain.util.NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Unknown error occurred"
                            )
                        }
                    }
                    is com.booktalk.domain.util.NetworkResult.Loading -> {
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
}
