package com.booktalk.ui.discovery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.booktalk.domain.model.NetworkResult
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.repository.BookRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookDiscoveryViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var bookRepository: BookRepository
    private lateinit var viewModel: BookDiscoveryViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        bookRepository = mockk(relaxed = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = BookDiscoveryViewModel(bookRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertNull(state.selectedGenre)
        assertEquals(emptyList<Book>(), state.searchResults)
        assertEquals(emptyList<Book>(), state.recommendedBooks)
        assertEquals(emptyList<Book>(), state.popularBooks)
        assertEquals(emptyList<Book>(), state.genreBooks)
        assertEquals(true, state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `search query updates state and triggers search`() = runTest {
        val testQuery = "test query"
        val testBooks = listOf(
            Book(
                id = "1",
                title = "Test Book",
                authors = listOf("Test Author"),
                description = "Test Description",
                imageUrl = "http://example.com/image.jpg",
                categories = listOf("Fiction"),
                averageRating = 4.5f,
                pageCount = 100,
                currentPage = 0,
                status = null,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )

        coEvery { bookRepository.searchBooks(testQuery) } returns flowOf(NetworkResult.Success(testBooks))

        viewModel.onSearchQueryChange(testQuery)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(testQuery, state.searchQuery)
        assertEquals(testBooks, state.searchResults)
        assertEquals(false, state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `selecting genre updates state and triggers book fetch`() = runTest {
        val testGenre = "Fiction"
        val testBooks = listOf(
            Book(
                id = "1",
                title = "Test Book",
                authors = listOf("Test Author"),
                description = "Test Description",
                imageUrl = "http://example.com/image.jpg",
                categories = listOf("Fiction"),
                averageRating = 4.5f,
                pageCount = 100,
                currentPage = 0,
                status = null,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )

        coEvery { bookRepository.getBooksByGenre(testGenre) } returns flowOf(NetworkResult.Success(testBooks))

        viewModel.onGenreSelected(testGenre)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(testGenre, state.selectedGenre)
        assertEquals(testBooks, state.genreBooks)
        assertEquals(false, state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `error in search updates error state`() = runTest {
        val testQuery = "test query"
        val errorMessage = "Network error"

        coEvery { bookRepository.searchBooks(testQuery) } returns flowOf(NetworkResult.Error(errorMessage))

        viewModel.onSearchQueryChange(testQuery)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(testQuery, state.searchQuery)
        assertEquals(emptyList<Book>(), state.searchResults)
        assertEquals(false, state.isLoading)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `clear search resets search state`() = runTest {
        // First set some search state
        viewModel.onSearchQueryChange("test query")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then clear it
        viewModel.clearSearch()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertEquals(emptyList<Book>(), state.searchResults)
        assertNull(state.error)
    }
}
