package com.booktalk.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booktalk.domain.model.auth.AuthResult
import com.booktalk.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        checkAuthState()
        observeAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            _uiState.update { it.copy(user = user) }
        }
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.isAuthenticated().collect { isAuthenticated ->
                _uiState.update { it.copy(isAuthenticated = isAuthenticated) }
            }
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun onDisplayNameChange(displayName: String) {
        _uiState.update { it.copy(displayName = displayName) }
    }

    fun signInWithEmail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = authRepository.signInWithEmail(_uiState.value.email, _uiState.value.password)) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(user = result.data, isLoading = false) }
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(error = result.message, isLoading = false) }
                }
                AuthResult.Loading -> Unit // Handled by isLoading state
            }
        }
    }

    fun signUpWithEmail() {
        if (_uiState.value.password != _uiState.value.confirmPassword) {
            _uiState.update { it.copy(error = "Passwords do not match") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = authRepository.signUpWithEmail(
                _uiState.value.email,
                _uiState.value.password,
                _uiState.value.displayName
            )) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(user = result.data, isLoading = false) }
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(error = result.message, isLoading = false) }
                }
                AuthResult.Loading -> Unit // Handled by isLoading state
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.update { AuthUiState() }
        }
    }

    fun resetPassword() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = authRepository.resetPassword(_uiState.value.email)) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        message = "Password reset email sent. Please check your inbox."
                    )}
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(error = result.message, isLoading = false) }
                }
                AuthResult.Loading -> Unit // Handled by isLoading state
            }
        }
    }

    fun updatePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = authRepository.updatePassword(oldPassword, newPassword)) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        message = "Password updated successfully"
                    )}
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(error = result.message, isLoading = false) }
                }
                AuthResult.Loading -> Unit // Handled by isLoading state
            }
        }
    }
}
