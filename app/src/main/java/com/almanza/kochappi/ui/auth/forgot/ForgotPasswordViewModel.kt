package com.almanza.kochappi.ui.auth.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.almanza.kochappi.data.remote.util.ErrorParser
import com.almanza.kochappi.domain.model.MessageResponse
import com.almanza.kochappi.domain.repository.AuthRepository
import com.almanza.kochappi.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _sendCodeState = MutableStateFlow<UiState<MessageResponse>>(UiState.Idle)
    val sendCodeState: StateFlow<UiState<MessageResponse>> = _sendCodeState.asStateFlow()

    private val _resetPasswordState = MutableStateFlow<UiState<MessageResponse>>(UiState.Idle)
    val resetPasswordState: StateFlow<UiState<MessageResponse>> = _resetPasswordState.asStateFlow()

    fun sendCode(email: String) {
        if (_sendCodeState.value is UiState.Loading) return
        viewModelScope.launch {
            _sendCodeState.value = UiState.Loading
            _sendCodeState.value = try {
                val result = authRepository.forgotPassword(email)
                UiState.Success(result)
            } catch (e: Exception) {
                val errorMessage = ErrorParser.parseError(e)
                UiState.Error(errorMessage)
            }
        }
    }

    fun resetPassword(email: String, otpCode: String, newPassword: String) {
        if (_resetPasswordState.value is UiState.Loading) return
        viewModelScope.launch {
            _resetPasswordState.value = UiState.Loading
            _resetPasswordState.value = try {
                val result = authRepository.resetPassword(email, otpCode, newPassword)
                UiState.Success(result)
            } catch (e: Exception) {
                val errorMessage = ErrorParser.parseError(e)
                UiState.Error(errorMessage)
            }
        }
    }
}
