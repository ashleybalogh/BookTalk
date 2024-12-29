package com.booktalk.ui.navigation

object NavRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val BOOK_DETAILS = "book_details/{bookId}"
    const val DISCOVERY = "discovery"

    fun bookDetails(bookId: String) = "book_details/$bookId"
}
