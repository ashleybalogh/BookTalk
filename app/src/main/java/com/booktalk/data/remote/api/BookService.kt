package com.booktalk.data.remote.api

import com.booktalk.data.model.BookDetailsResponse
import com.booktalk.data.model.BookSearchResponse
import com.booktalk.domain.model.book.Book
import retrofit2.Response
import retrofit2.http.*

interface BookService {
    @GET("books/search")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): Response<BookSearchResponse>

    @GET("books/{id}")
    suspend fun getBookById(
        @Path("id") id: String
    ): Response<BookDetailsResponse>

    @GET("books/isbn/{isbn}")
    suspend fun getBookByIsbn(
        @Path("isbn") isbn: String
    ): Response<BookDetailsResponse>

    @GET("books/category/{category}")
    suspend fun getBooksByCategory(
        @Path("category") category: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): Response<BookSearchResponse>

    @GET("books/recommended/{userId}")
    suspend fun getRecommendedBooks(
        @Path("userId") userId: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): Response<BookSearchResponse>

    @POST("books")
    suspend fun saveBook(
        @Header("Authorization") token: String,
        @Body book: Book
    ): Response<BookDetailsResponse>

    @PUT("books/{id}/progress")
    suspend fun updateReadingProgress(
        @Header("Authorization") token: String,
        @Path("id") bookId: String,
        @Body currentPage: Int,
        @Body totalPages: Int
    ): Response<Unit>

    @PUT("books/{id}/status")
    suspend fun updateBookStatus(
        @Header("Authorization") token: String,
        @Path("id") bookId: String,
        @Body status: String
    ): Response<Unit>

    @POST("books/{id}/rating")
    suspend fun rateBook(
        @Header("Authorization") token: String,
        @Path("id") bookId: String,
        @Body rating: Float
    ): Response<Unit>

    @POST("books/{id}/notes")
    suspend fun addBookNote(
        @Header("Authorization") token: String,
        @Path("id") bookId: String,
        @Body note: String
    ): Response<Unit>
}
