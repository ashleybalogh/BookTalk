package com.booktalk.data.repository

import com.booktalk.data.local.secure.SecureStorage
import com.booktalk.data.remote.api.AuthApiService
import com.booktalk.data.remote.dto.auth.LoginRequest
import com.booktalk.domain.model.auth.AuthResult
import com.booktalk.domain.model.auth.User
import com.booktalk.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val secureStorage: SecureStorage
) : AuthRepository {
    private val mockUser = MutableStateFlow<User?>(null)

    override suspend fun signInWithEmail(email: String, password: String): AuthResult<User> {
        return try {
            val loginRequest = LoginRequest(email, password)
            val response = authApiService.login(loginRequest)
            
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    secureStorage.saveToken(authResponse.token)
                    secureStorage.saveRefreshToken(authResponse.refreshToken)
                    secureStorage.saveUserId(authResponse.userId)
                    
                    // Get user details
                    val userResponse = authApiService.getCurrentUser("Bearer ${authResponse.token}")
                    if (userResponse.isSuccessful) {
                        userResponse.body()?.let { user ->
                            val currentUser = User(
                                id = user.id,
                                email = user.email,
                                displayName = user.displayName,
                                photoUrl = user.photoUrl,
                                emailVerified = user.emailVerified
                            )
                            mockUser.value = currentUser
                            AuthResult.Success(currentUser)
                        } ?: AuthResult.Error("Failed to get user details")
                    } else {
                        AuthResult.Error(userResponse.message() ?: "Failed to get user details")
                    }
                } ?: AuthResult.Error("Empty response from server")
            } else {
                AuthResult.Error(response.message() ?: "Login failed")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String
    ): AuthResult<User> {
        // Simulate network delay
        delay(1000)

        return if (email.contains("@") && password.length >= 6) {
            val user = User(
                id = "1",
                email = email,
                displayName = displayName
            )
            secureStorage.saveToken("mock_token")
            secureStorage.saveUserId(user.id)
            mockUser.value = user
            AuthResult.Success(user)
        } else {
            AuthResult.Error("Invalid email or password")
        }
    }

    override suspend fun signOut() {
        delay(500) // Simulate network delay
        secureStorage.clearAll()
        mockUser.value = null
    }

    override suspend fun getCurrentUser(): User? {
        return mockUser.value ?: secureStorage.getUserId()?.let { userId ->
            User(
                id = userId,
                email = "test@example.com",
                displayName = "Test User"
            ).also {
                mockUser.value = it
            }
        }
    }

    override fun isAuthenticated(): Flow<Boolean> {
        return MutableStateFlow(secureStorage.getToken() != null)
    }

    override suspend fun resetPassword(email: String): AuthResult<Unit> {
        delay(1000) // Simulate network delay
        return if (email.contains("@")) {
            AuthResult.Success(Unit)
        } else {
            AuthResult.Error("Invalid email address")
        }
    }

    override suspend fun updatePassword(oldPassword: String, newPassword: String): AuthResult<Unit> {
        delay(1000) // Simulate network delay
        return if (oldPassword == "password123" && newPassword.length >= 6) {
            AuthResult.Success(Unit)
        } else {
            AuthResult.Error("Invalid password")
        }
    }

    override suspend fun deleteAccount() {
        // Not implemented in API yet
        secureStorage.clearAll()
    }

    override suspend fun verifyPasswordResetCode(code: String): AuthResult<Unit> {
        // Not implemented in API yet
        return AuthResult.Error("Not implemented")
    }

    override suspend fun refreshToken(): AuthResult<Unit> {
        return try {
            val refreshToken = secureStorage.getRefreshToken()
                ?: return AuthResult.Error("No refresh token available")

            val response = authApiService.refreshToken("Bearer $refreshToken")
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    // Store new tokens
                    secureStorage.saveToken(authResponse.token)
                    secureStorage.saveRefreshToken(authResponse.refreshToken)
                    secureStorage.saveUserId(authResponse.userId)
                    // Return success
                    AuthResult.Success(Unit)
                } ?: AuthResult.Error("Empty response from server")
            } else {
                // Clear tokens if refresh failed
                secureStorage.clearAll()
                AuthResult.Error(response.message() ?: "Failed to refresh token")
            }
        } catch (e: Exception) {
            // Clear tokens if refresh failed
            secureStorage.clearAll()
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun updateProfile(displayName: String?, photoUrl: String?): AuthResult<User> {
        return try {
            val token = secureStorage.getToken() ?: return AuthResult.Error("Not authenticated")
            val response = authApiService.updateProfile("Bearer $token", displayName, photoUrl)
            if (response.isSuccessful) {
                response.body()?.let { userResponse ->
                    AuthResult.Success(User(
                        id = userResponse.id,
                        email = userResponse.email,
                        displayName = userResponse.displayName,
                        photoUrl = userResponse.photoUrl,
                        emailVerified = userResponse.emailVerified
                    ))
                } ?: AuthResult.Error("Empty response from server")
            } else {
                AuthResult.Error(response.message() ?: "Failed to update profile")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun updateEmail(newEmail: String, password: String): AuthResult<Unit> {
        // Not implemented in API yet
        return AuthResult.Error("Not implemented")
    }

    override suspend fun sendEmailVerification(): AuthResult<Unit> {
        return try {
            val token = secureStorage.getToken() ?: return AuthResult.Error("Not authenticated")
            val response = authApiService.sendEmailVerification("Bearer $token")
            if (response.isSuccessful) {
                AuthResult.Success(Unit)
            } else {
                AuthResult.Error(response.message() ?: "Failed to send verification email")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getAccessToken(): String? = secureStorage.getToken()

    override suspend fun clearTokens() = secureStorage.clearAll()
}
