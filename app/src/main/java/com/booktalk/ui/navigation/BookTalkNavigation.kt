package com.booktalk.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.booktalk.ui.auth.LoginScreen
import com.booktalk.ui.auth.RegisterScreen
import com.booktalk.ui.bookdetails.BookDetailsScreen
import com.booktalk.ui.discovery.BookDiscoveryScreen
import com.booktalk.ui.readinglist.ReadingListScreen

@Composable
fun BookTalkNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Discovery.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Discovery.route) {
            BookDiscoveryScreen(
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetails.createRoute(bookId))
                }
            )
        }

        composable(Screen.ReadingList.route) {
            ReadingListScreen(
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetails.createRoute(bookId))
                }
            )
        }

        composable(
            route = Screen.BookDetails.route,
            arguments = listOf(
                navArgument(Screen.BOOK_ID_KEY) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            BookDetailsScreen(
                bookId = backStackEntry.arguments?.getString(Screen.BOOK_ID_KEY) ?: "",
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Discovery.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Discovery.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Discovery : Screen("discovery")
    object ReadingList : Screen("reading_list")
    object BookDetails : Screen("book_details") {
        const val BOOK_ID_KEY = "bookId"
        fun createRoute(bookId: String): String {
            return "$route/$bookId"
        }
    }
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
}
