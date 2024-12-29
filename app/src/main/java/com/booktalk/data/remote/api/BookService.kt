package com.booktalk.data.remote.api

import com.booktalk.data.model.BookDto
import com.booktalk.data.model.BooksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookService {
    @GET("books/search")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 10
    ): Response<BooksResponse>

    @GET("books/{id}")
    suspend fun getBookById(
        @Path("id") id: String
    ): Response<BookDto>

    @GET("books/{isbn}")
    suspend fun getBookByIsbn(
        @Path("isbn") isbn: String
    ): Response<BookDto>

    @GET("books/category/{category}")
    suspend fun getBooksByCategory(
        @Path("category") category: String,
        @Query("maxResults") maxResults: Int = 10
    ): Response<BooksResponse>

    @GET("books/recommended/{userId}")
    suspend fun getRecommendedBooks(
        @Path("userId") userId: String,
        @Query("maxResults") maxResults: Int = 10
    ): Response<BooksResponse>
}
