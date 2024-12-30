package com.booktalk.ui.readinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.model.book.ReadingStatus
import com.booktalk.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingListViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReadingListUiState())
    val uiState: StateFlow<ReadingListUiState> = _uiState.asStateFlow()

    init {
        loadBooks()
    }

    fun loadBooks(status: ReadingStatus? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val books = if (status != null) {
                    bookRepository.getUserBooksByStatus("currentUser", status)
                } else {
                    bookRepository.getUserBooks("currentUser")
                }
                
                books.collect { result ->
                    _uiState.update { currentState -> 
                        currentState.copy(
                            isLoading = false,
                            books = result,
                            selectedStatus = status,
                            filterOptions = ReadingStatus.values().toList()
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load books"
                    )
                }
            }
        }
    }

    fun updateBookStatus(book: Book, newStatus: ReadingStatus) {
        viewModelScope.launch {
            try {
                bookRepository.updateBookStatus("currentUser", book.id!!, newStatus)
                loadBooks(_uiState.value.selectedStatus)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to update book status")
                }
            }
        }
    }

    fun updateReadingProgress(book: Book, currentPage: Int) {
        viewModelScope.launch {
            try {
                bookRepository.updateReadingProgress(
                    "currentUser",
                    book.id!!,
                    currentPage,
                    book.pageCount!!
                )
                loadBooks(_uiState.value.selectedStatus)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to update reading progress")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
