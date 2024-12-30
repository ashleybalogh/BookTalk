package com.booktalk.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.booktalk.domain.model.book.Book
import com.booktalk.ui.auth.LoginScreen
import com.booktalk.ui.auth.RegisterScreen
import com.booktalk.ui.bookdetails.BookDetailsScreen
import com.booktalk.ui.discovery.BookDiscoveryScreen
import com.booktalk.ui.readinglist.ReadingListScreen
import com.booktalk.ui.navigation.Screen

@Composable
fun BookTalkNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Discovery.route) {
            BookDiscoveryScreen(
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetails.createRoute(bookId))
                }
            )
        }

        composable(
            route = Screen.BookDetails.route,
            arguments = listOf(
                navArgument(Screen.BookDetails.BOOK_ID_KEY) { type = NavType.StringType }
            )
        ) {
            BookDetailsScreen(
                viewModel = hiltViewModel(),
                onBackClick = { navController.navigateUp() },
                onStartReading = { _ ->
                    // Handle start reading action
                    navController.navigate(Screen.ReadingList.route)
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

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Discovery.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    // TODO: Implement forgot password navigation
                    navController.navigate(Screen.ForgotPassword.route)
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
                onNavigateToLogin = {
                    navController.navigateUp()
                }
            )
        }
    }
}