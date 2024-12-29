package com.booktalk.data.repository

import com.booktalk.data.local.dao.BookDao
import com.booktalk.data.local.dao.UserBookDao
import com.booktalk.data.mapper.toBook
import com.booktalk.data.mapper.toEntity
import com.booktalk.data.remote.util.NetworkResult
import com.booktalk.domain.model.Book
import com.booktalk.data.source.BookDataSource
import io.mockk.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Named

class BookRepositoryTest {
    private lateinit var localDataSource: BookDataSource
    private lateinit var remoteDataSource: BookDataSource
    private lateinit var bookDao: BookDao
    private lateinit var userBookDao: UserBookDao
    private lateinit var repository: BookRepositoryImpl

    @Before
    fun setup() {
        localDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        bookDao = mockk(relaxed = true)
        userBookDao = mockk(relaxed = true)
        repository = BookRepositoryImpl(localDataSource, remoteDataSource, bookDao, userBookDao)
    }

    @Test
    fun `searchBooks returns books from database and network`() = runTest {
        // Given
        val query = "test"
        val book = createTestBook()
        val bookEntity = book.toEntity()
        
        // Mock database response
        every { bookDao.searchBooksFlow("%$query%") } returns flowOf(listOf(bookEntity))
        
        // Mock network response
        coEvery { remoteDataSource.searchBooks(query) } returns listOf(book)
        
        // Mock database operations
        coEvery { bookDao.insertBooks(any()) } just Runs

        // When
        val results = repository.searchBooks(query).toList()

        // Then
        assertEquals(3, results.size)
        assertTrue(results[0] is NetworkResult.Loading)
        assertTrue(results[1] is NetworkResult.Success)
        assertTrue(results[2] is NetworkResult.Success)
        assertEquals(listOf(book), (results[2] as NetworkResult.Success<List<Book>>).data)
        
        // Verify database was updated with network results
        coVerify { bookDao.insertBooks(any()) }
    }

    @Test
    fun `getBookById returns book from database and network`() = runTest {
        // Given
        val bookId = "1"
        val book = createTestBook()
        val bookEntity = book.toEntity()
        
        // Mock database response
        coEvery { bookDao.getBookById(bookId) } returns bookEntity
        
        // Mock network response
        coEvery { remoteDataSource.getBookById(bookId) } returns book
        
        // Mock database operations
        coEvery { bookDao.insertBook(any()) } just Runs

        // When
        val results = repository.getBookById(bookId).toList()

        // Then
        assertEquals(3, results.size)
        assertTrue(results[0] is NetworkResult.Loading)
        assertTrue(results[1] is NetworkResult.Success)
        assertTrue(results[2] is NetworkResult.Success)
        assertEquals(book, (results[2] as NetworkResult.Success<Book>).data)
        
        // Verify database was updated with network result
        coVerify { bookDao.insertBook(any()) }
    }

    private fun createTestBook() = Book(
        id = "1",
        title = "Test Book",
        author = "Test Author",
        description = "Test Description",
        coverUrl = "https://example.com/cover.jpg",
        isbn = "1234567890",
        pageCount = 200,
        publishedDate = "2023",
        publisher = "Test Publisher",
        categories = listOf("Test Category"),
        category = "Fiction",
        language = "en",
        averageRating = 4.5f,
        ratingsCount = 100
    )
}
