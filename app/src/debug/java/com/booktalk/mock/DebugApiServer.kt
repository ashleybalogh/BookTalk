package com.booktalk.mock

import android.content.Context
import android.util.Log
import com.booktalk.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.json.JSONObject
import java.net.InetAddress
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebugApiServer @Inject constructor(
    @ApplicationContext private val context: Context
) : ApiServer {
    private val server = MockWebServer()
    private val serverStarted = CountDownLatch(1)
    
    init {
        setupMockServer()
    }
    
    private fun setupMockServer() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/auth/login" -> handleLogin(request)
                    "/auth/register" -> handleRegister(request)
                    else -> MockResponse().setResponseCode(404)
                }
            }
        }
        
        // Start server on a background thread
        Thread {
            try {
                // Bind to all interfaces to ensure it's accessible from the emulator
                server.start(InetAddress.getByName("0.0.0.0"), 8080)
                serverStarted.countDown()
                Log.d("DebugApiServer", "Mock server started successfully")
            } catch (e: Exception) {
                Log.e("DebugApiServer", "Failed to start mock server", e)
            }
        }.start()

        // Wait for server to start (with timeout)
        if (!serverStarted.await(5, TimeUnit.SECONDS)) {
            Log.e("DebugApiServer", "Timeout waiting for mock server to start")
        }
    }
    
    private fun handleLogin(request: RecordedRequest): MockResponse {
        val body = request.body.readUtf8()
        val json = JSONObject(body)
        val email = json.getString("email")
        val password = json.getString("password")
        
        Log.d("DebugApiServer", "Login attempt: $email")
        
        // Mock successful login for test@example.com/password123
        return if (email == "test@example.com" && password == "password123") {
            MockResponse()
                .setResponseCode(200)
                .setBody("""
                    {
                        "token": "mock-jwt-token",
                        "refreshToken": "mock-refresh-token",
                        "userId": "1",
                        "user": {
                            "id": "1",
                            "email": "$email",
                            "displayName": "Test User"
                        }
                    }
                """.trimIndent())
        } else {
            MockResponse()
                .setResponseCode(401)
                .setBody("""
                    {
                        "error": "Invalid credentials"
                    }
                """.trimIndent())
        }
    }
    
    private fun handleRegister(request: RecordedRequest): MockResponse {
        val body = request.body.readUtf8()
        val json = JSONObject(body)
        val email = json.getString("email")
        
        Log.d("DebugApiServer", "Register attempt: $email")
        
        return MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "token": "mock-jwt-token",
                    "refreshToken": "mock-refresh-token",
                    "userId": "1",
                    "user": {
                        "id": "1",
                        "email": "$email",
                        "displayName": "New User"
                    }
                }
            """.trimIndent())
    }
    
    override fun shutdown() {
        server.shutdown()
    }
}
