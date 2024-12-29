package com.booktalk.ui.readinglist

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

data class BookListUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookListUiState())
    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()

    init {
        loadReadingList()
    }

    private fun loadReadingList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            bookRepository.getReadingList().collect { result: NetworkResult<List<Book>> ->
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
                                error = result.message ?: "Unknown error occurred"
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
}
