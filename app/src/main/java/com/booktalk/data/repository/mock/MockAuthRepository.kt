package com.booktalk.data.repository.mock

import com.booktalk.domain.model.auth.AuthResult
import com.booktalk.domain.model.auth.User
import com.booktalk.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockAuthRepository @Inject constructor() : AuthRepository {
    private val mockUser = User(
        id = "mock-user-id",
        email = "mock@example.com",
        displayName = "Mock User",
        photoUrl = null,
        emailVerified = false
    )

    private val _isAuthenticated = MutableStateFlow(false)

    override suspend fun signInWithEmail(email: String, password: String): AuthResult<User> {
        delay(1000) // Simulate network delay
        _isAuthenticated.value = true
        return AuthResult.Success(mockUser)
    }

    override suspend fun signUpWithEmail(email: String, password: String, displayName: String): AuthResult<User> {
        delay(1000)
        _isAuthenticated.value = true
        return AuthResult.Success(mockUser.copy(displayName = displayName, email = email))
    }

    override suspend fun signOut() {
        _isAuthenticated.value = false
    }

    override suspend fun deleteAccount() {
        _isAuthenticated.value = false
    }

    override suspend fun resetPassword(email: String): AuthResult<Unit> {
        delay(1000)
        return AuthResult.Success(Unit)
    }

    override suspend fun updatePassword(oldPassword: String, newPassword: String): AuthResult<Unit> {
        delay(1000)
        return AuthResult.Success(Unit)
    }

    override suspend fun verifyPasswordResetCode(code: String): AuthResult<Unit> {
        delay(1000)
        return AuthResult.Success(Unit)
    }

    override suspend fun refreshToken(): AuthResult<Unit> {
        delay(1000)
        return AuthResult.Success(Unit)
    }

    override fun isAuthenticated(): Flow<Boolean> = _isAuthenticated

    override suspend fun getCurrentUser(): User? {
        return if (_isAuthenticated.value) mockUser else null
    }

    override suspend fun updateProfile(displayName: String?, photoUrl: String?): AuthResult<User> {
        delay(1000)
        return AuthResult.Success(mockUser.copy(
            displayName = displayName ?: mockUser.displayName,
            photoUrl = photoUrl ?: mockUser.photoUrl
        ))
    }

    override suspend fun updateEmail(newEmail: String, password: String): AuthResult<Unit> {
        delay(1000)
        return AuthResult.Success(Unit)
    }

    override suspend fun sendEmailVerification(): AuthResult<Unit> {
        delay(1000)
        return AuthResult.Success(Unit)
    }

    override suspend fun getAccessToken(): String? {
        return if (_isAuthenticated.value) "mock-access-token" else null
    }

    override suspend fun clearTokens() {
        _isAuthenticated.value = false
    }
}
