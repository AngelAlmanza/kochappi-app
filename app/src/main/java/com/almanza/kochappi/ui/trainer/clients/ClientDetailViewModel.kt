package com.almanza.kochappi.ui.trainer.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.almanza.kochappi.domain.model.Customer
import com.almanza.kochappi.domain.repository.CustomerRepository
import com.almanza.kochappi.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientDetailViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Customer>>(UiState.Idle)
    val uiState: StateFlow<UiState<Customer>> = _uiState.asStateFlow()

    private val _deleteState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteState: StateFlow<UiState<Unit>> = _deleteState.asStateFlow()

    fun loadClient(id: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = try {
                UiState.Success(customerRepository.getById(id))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al cargar cliente")
            }
        }
    }

    fun deleteClient(id: Int) {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            _deleteState.value = try {
                customerRepository.delete(id)
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al eliminar cliente")
            }
        }
    }
}
