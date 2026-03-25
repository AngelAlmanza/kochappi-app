package com.almanza.kochappi.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.almanza.kochappi.data.remote.util.ErrorParser
import com.almanza.kochappi.domain.model.Session
import com.almanza.kochappi.domain.repository.AuthRepository
import com.almanza.kochappi.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Session>>(UiState.Idle)
    val uiState: StateFlow<UiState<Session>> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (_uiState.value is UiState.Loading) return
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = try {
                val session = authRepository.login(email, password)
                UiState.Success(session)
            } catch (e: Exception) {
                val errorMessage = ErrorParser.parseError(e)
                UiState.Error(errorMessage)
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }
}
