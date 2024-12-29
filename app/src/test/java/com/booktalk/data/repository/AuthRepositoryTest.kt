package com.booktalk.data.repository

import com.booktalk.data.local.secure.SecureStorage
import com.booktalk.data.remote.api.AuthApiService
import com.booktalk.data.remote.dto.auth.AuthResponse
import com.booktalk.data.remote.dto.auth.LoginRequest
import com.booktalk.data.remote.dto.auth.UserResponse
import com.booktalk.domain.model.auth.AuthResult
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthRepositoryTest {
    private lateinit var authApiService: AuthApiService
    private lateinit var secureStorage: SecureStorage
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setup() {
        authApiService = mockk()
        secureStorage = mockk()
        repository = AuthRepositoryImpl(authApiService, secureStorage)
    }

    @Test
    fun `signInWithEmail returns success with user when API call succeeds`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val authResponse = createAuthResponse()
        val userResponse = createUserResponse()
        
        coEvery { authApiService.login(LoginRequest(email, password)) } returns Response.success(authResponse)
        coEvery { authApiService.getCurrentUser("Bearer ${authResponse.token}") } returns Response.success(userResponse)
        coEvery { secureStorage.saveToken(any()) } just Runs
        coEvery { secureStorage.saveRefreshToken(any()) } just Runs
        coEvery { secureStorage.saveUserId(any()) } just Runs
        coEvery { secureStorage.getToken() } returns authResponse.token

        // When
        val result = repository.signInWithEmail(email, password)

        // Then
        assertTrue(result is AuthResult.Success)
        assertNotNull((result as AuthResult.Success).data)
        assertEquals(userResponse.id, result.data?.id)
        coVerify { 
            secureStorage.saveToken(authResponse.token)
            secureStorage.saveRefreshToken(authResponse.refreshToken)
            secureStorage.saveUserId(authResponse.userId)
        }
    }

    @Test
    fun `signInWithEmail returns error when API call fails`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        coEvery { authApiService.login(LoginRequest(email, password)) } returns Response.error(
            401,
            okhttp3.ResponseBody.create(null, "Invalid credentials")
        )

        // When
        val result = repository.signInWithEmail(email, password)

        // Then
        assertTrue(result is AuthResult.Error)
        assertNotNull((result as AuthResult.Error).message)
    }

    @Test
    fun `signOut clears tokens even when API call fails`() = runTest {
        // Given
        coEvery { secureStorage.getToken() } returns "token"
        coEvery { authApiService.logout(any()) } throws Exception("Network error")
        coEvery { secureStorage.clearAll() } just Runs

        // When
        repository.signOut()

        // Then
        coVerify { secureStorage.clearAll() }
    }

    @Test
    fun `isAuthenticated emits true when access token exists`() = runTest {
        // Given
        coEvery { secureStorage.getToken() } returns "valid_token"

        // When
        val isAuthenticated = repository.isAuthenticated().first()

        // Then
        assertTrue(isAuthenticated)
    }

    @Test
    fun `isAuthenticated emits false when access token is null`() = runTest {
        // Given
        coEvery { secureStorage.getToken() } returns null

        // When
        val isAuthenticated = repository.isAuthenticated().first()

        // Then
        assertFalse(isAuthenticated)
    }

    @Test
    fun `refreshToken updates tokens when successful`() = runTest {
        // Given
        val refreshToken = "old_refresh_token"
        val authResponse = createAuthResponse()
        val userResponse = createUserResponse()
        
        coEvery { secureStorage.getRefreshToken() } returns refreshToken
        coEvery { authApiService.refreshToken("Bearer $refreshToken") } returns Response.success(authResponse)
        coEvery { authApiService.getCurrentUser("Bearer ${authResponse.token}") } returns Response.success(userResponse)
        coEvery { secureStorage.saveToken(any()) } just Runs
        coEvery { secureStorage.saveRefreshToken(any()) } just Runs
        coEvery { secureStorage.saveUserId(any()) } just Runs
        coEvery { secureStorage.getToken() } returns authResponse.token

        // When
        val result = repository.refreshToken()

        // Then
        assertTrue(result is AuthResult.Success)
        coVerify { 
            secureStorage.saveToken(authResponse.token)
            secureStorage.saveRefreshToken(authResponse.refreshToken)
            secureStorage.saveUserId(authResponse.userId)
        }
    }

    @Test
    fun `refreshToken clears tokens when refresh fails`() = runTest {
        // Given
        val refreshToken = "old_refresh_token"
        coEvery { secureStorage.getRefreshToken() } returns refreshToken
        coEvery { authApiService.refreshToken("Bearer $refreshToken") } returns Response.error(
            401,
            okhttp3.ResponseBody.create(null, "Invalid refresh token")
        )
        coEvery { secureStorage.clearAll() } just Runs

        // When
        val result = repository.refreshToken()

        // Then
        assertTrue(result is AuthResult.Error)
        coVerify { secureStorage.clearAll() }
    }

    private fun createAuthResponse() = AuthResponse(
        userId = "1",
        token = "access_token",
        refreshToken = "refresh_token",
        expiresIn = 3600L
    )

    private fun createUserResponse() = UserResponse(
        id = "1",
        email = "test@example.com",
        displayName = "Test User",
        photoUrl = "https://example.com/photo.jpg",
        emailVerified = true,
        createdAt = System.currentTimeMillis(),
        lastLoginAt = System.currentTimeMillis()
    )
}
