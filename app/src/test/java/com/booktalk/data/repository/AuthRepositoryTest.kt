package com.booktalk.data.repository

import com.booktalk.data.local.TokenManager
import com.booktalk.data.model.AuthResponse
import com.booktalk.data.model.UserResponse
import com.booktalk.data.remote.api.AuthService
import com.booktalk.domain.model.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthRepositoryTest {
    private lateinit var authService: AuthService
    private lateinit var tokenManager: TokenManager
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setup() {
        authService = mockk()
        tokenManager = mockk(relaxed = true)
        repository = AuthRepositoryImpl(authService, tokenManager)
    }

    @Test
    fun `signInWithEmail returns success with user when API call succeeds`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val authResponse = createAuthResponse()
        val userResponse = createUserResponse()

        coEvery {
            authService.signInWithEmail(any())
        } returns Response.success(authResponse)

        coEvery {
            authService.getUser()
        } returns Response.success(userResponse)

        val result = repository.signInWithEmail(email, password).first()

        assertTrue(result is NetworkResult.Success)
        assertEquals(userResponse.toUser(), (result as NetworkResult.Success).data)
        coVerify { tokenManager.saveTokens(authResponse.accessToken, authResponse.refreshToken) }
    }

    @Test
    fun `signInWithEmail returns error when API call fails`() = runTest {
        val email = "test@example.com"
        val password = "password123"

        coEvery {
            authService.signInWithEmail(any())
        } throws Exception("Network error")

        val result = repository.signInWithEmail(email, password).first()

        assertTrue(result is NetworkResult.Error)
        assertEquals("Network error", (result as NetworkResult.Error).message)
    }

    @Test
    fun `signOut clears tokens even when API call fails`() = runTest {
        coEvery {
            authService.signOut()
        } throws Exception("Network error")

        repository.signOut()

        coVerify { tokenManager.clearTokens() }
    }

    @Test
    fun `isAuthenticated emits true when access token exists`() = runTest {
        coEvery {
            tokenManager.getAccessToken()
        } returns "valid_token"

        val result = repository.isAuthenticated().first()

        assertTrue(result)
    }

    @Test
    fun `isAuthenticated emits false when access token is null`() = runTest {
        coEvery {
            tokenManager.getAccessToken()
        } returns null

        val result = repository.isAuthenticated().first()

        assertFalse(result)
    }

    @Test
    fun `refreshToken updates tokens when successful`() = runTest {
        val authResponse = createAuthResponse()

        coEvery {
            tokenManager.getRefreshToken()
        } returns "old_refresh_token"

        coEvery {
            authService.refreshToken(any())
        } returns Response.success(authResponse)

        val result = repository.refreshToken().first()

        assertTrue(result is NetworkResult.Success)
        coVerify { tokenManager.saveTokens(authResponse.accessToken, authResponse.refreshToken) }
    }

    @Test
    fun `refreshToken clears tokens when refresh fails`() = runTest {
        coEvery {
            tokenManager.getRefreshToken()
        } returns "old_refresh_token"

        coEvery {
            authService.refreshToken(any())
        } throws Exception("Refresh failed")

        val result = repository.refreshToken().first()

        assertTrue(result is NetworkResult.Error)
        coVerify { tokenManager.clearTokens() }
    }

    private fun createAuthResponse() = AuthResponse(
        accessToken = "access_token",
        refreshToken = "refresh_token",
        expiresIn = 3600
    )

    private fun createUserResponse() = UserResponse(
        id = "user_id",
        email = "test@example.com",
        name = "Test User",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}
