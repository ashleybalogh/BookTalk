package com.booktalk.domain.repository

import com.booktalk.domain.model.auth.AuthResult
import com.booktalk.domain.model.auth.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    // Authentication
    suspend fun signInWithEmail(email: String, password: String): AuthResult<User>
    suspend fun signUpWithEmail(email: String, password: String, displayName: String): AuthResult<User>
    suspend fun signOut()
    suspend fun deleteAccount()
    
    // Password management
    suspend fun resetPassword(email: String): AuthResult<Unit>
    suspend fun updatePassword(oldPassword: String, newPassword: String): AuthResult<Unit>
    suspend fun verifyPasswordResetCode(code: String): AuthResult<Unit>
    
    // Session management
    suspend fun refreshToken(): AuthResult<Unit>
    fun isAuthenticated(): Flow<Boolean>
    suspend fun getCurrentUser(): User?
    
    // Profile management
    suspend fun updateProfile(displayName: String? = null, photoUrl: String? = null): AuthResult<User>
    suspend fun updateEmail(newEmail: String, password: String): AuthResult<Unit>
    suspend fun sendEmailVerification(): AuthResult<Unit>
    
    // Token management
    suspend fun getAccessToken(): String?
    suspend fun clearTokens()
}
