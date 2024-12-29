package com.booktalk.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object Discovery : Screen("discovery")
    object BookDetails : Screen("book_details/{bookId}") {
        fun createRoute(bookId: String) = "book_details/$bookId"
    }
    object ReadingList : Screen("reading_list")
    
    companion object {
        const val BOOK_ID_KEY = "bookId"
    }
}
