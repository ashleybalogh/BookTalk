package com.booktalk.data.api

import com.booktalk.data.model.BookDto
import retrofit2.Response

interface BookApiService {
    suspend fun searchBooks(query: String, page: Int): Response<List<BookDto>>
    suspend fun getBooksByGenre(genre: String, page: Int): Response<List<BookDto>>
    suspend fun getBookById(id: String): Response<BookDto>
    suspend fun getRecommendedBooks(userId: String): Response<List<BookDto>>
}
