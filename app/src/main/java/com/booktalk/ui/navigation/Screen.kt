package com.booktalk.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object Discovery : Screen("discovery")
    object BookDetails : Screen("book_details/{bookId}") {
        const val BOOK_ID_KEY = "bookId"
        fun createRoute(bookId: String) = "book_details/$bookId"
    }
    object ReadingList : Screen("reading_list")
    object BookClub : Screen("book_club/{clubId}") {
        const val CLUB_ID_KEY = "clubId"
        fun createRoute(clubId: String) = "book_club/$clubId"
    }
}
