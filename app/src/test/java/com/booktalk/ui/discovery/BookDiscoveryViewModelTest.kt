package com.booktalk.ui.discovery

import com.booktalk.data.remote.util.NetworkResult
import com.booktalk.domain.model.book.Book
import com.booktalk.domain.model.book.ReadingStatus
import com.booktalk.domain.repository.BookRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BookDiscoveryViewModelTest {
    private lateinit var viewModel: BookDiscoveryViewModel
    private lateinit var bookRepository: BookRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        bookRepository = mockk()
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
        assertTrue(state.isLoading)
        assertNull(state.error)
        assertTrue(state.searchResults.isEmpty())
        assertTrue(state.recommendedBooks.isEmpty())
        assertTrue(state.popularBooks.isEmpty())
        assertTrue(state.genreBooks.isEmpty())
    }

    @Test
    fun `search query updates state and triggers search`() = runTest {
        val testQuery = "test query"
        val testBooks = listOf(
            Book(
                id = "1",
                title = "Test Book",
                author = "Test Author",
                genres = listOf("Fiction")
            )
        )

        coEvery { 
            bookRepository.searchBooks(testQuery)
        } returns flowOf(NetworkResult.Success(testBooks))

        viewModel.onSearchQueryChange(testQuery)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(testQuery, state.searchQuery)
        assertEquals(testBooks, state.searchResults)
        assertNull(state.error)
        assertTrue(!state.isLoading)
    }

    @Test
    fun `genre selection updates state and loads genre books`() = runTest {
        val testGenre = "Fiction"
        val testBooks = listOf(
            Book(
                id = "1",
                title = "Fiction Book",
                author = "Fiction Author",
                genres = listOf(testGenre)
            )
        )

        coEvery { 
            bookRepository.getBooksByGenre(testGenre)
        } returns flowOf(NetworkResult.Success(testBooks))

        viewModel.onGenreSelected(testGenre)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(testGenre, state.selectedGenre)
        assertEquals(testBooks, state.genreBooks)
        assertNull(state.error)
        assertTrue(!state.isLoading)
    }

    @Test
    fun `clear genre resets genre-related state`() = runTest {
        // First set a genre
        val testGenre = "Fiction"
        coEvery { 
            bookRepository.getBooksByGenre(testGenre)
        } returns flowOf(NetworkResult.Success(listOf()))

        viewModel.onGenreSelected(testGenre)
        testScheduler.advanceUntilIdle()

        // Then clear it
        viewModel.clearGenre()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.selectedGenre)
        assertTrue(state.genreBooks.isEmpty())
    }

    @Test
    fun `clear search resets search-related state`() = runTest {
        // First perform a search
        val testQuery = "test"
        coEvery { 
            bookRepository.searchBooks(testQuery)
        } returns flowOf(NetworkResult.Success(listOf()))

        viewModel.onSearchQueryChange(testQuery)
        testScheduler.advanceUntilIdle()

        // Then clear it
        viewModel.clearSearch()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertTrue(state.searchResults.isEmpty())
    }

    @Test
    fun `error in search updates error state`() = runTest {
        val testQuery = "test"
        val errorMessage = "Network error"
        
        coEvery { 
            bookRepository.searchBooks(testQuery)
        } returns flowOf(NetworkResult.Error(errorMessage))

        viewModel.onSearchQueryChange(testQuery)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(errorMessage, state.error)
        assertTrue(state.searchResults.isEmpty())
        assertTrue(!state.isLoading)
    }

    @Test
    fun `loading popular books on init`() = runTest {
        val testBooks = listOf(
            Book(
                id = "1",
                title = "Popular Book",
                author = "Popular Author",
                genres = listOf("Fiction"),
                popularity = 0.9f
            )
        )

        coEvery { 
            bookRepository.getAllBooks()
        } returns flowOf(NetworkResult.Success(testBooks))

        // Create a new ViewModel to trigger init
        val newViewModel = BookDiscoveryViewModel(bookRepository)
        testScheduler.advanceUntilIdle()

        val state = newViewModel.uiState.value
        assertEquals(testBooks, state.popularBooks)
        assertNull(state.error)
        assertTrue(!state.isLoading)
    }
}
