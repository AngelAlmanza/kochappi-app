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
class ClientListViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Customer>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<Customer>>> = _uiState.asStateFlow()

    init {
        loadClients()
    }

    fun loadClients() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = try {
                UiState.Success(customerRepository.getAll())
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al cargar clientes")
            }
        }
    }

    fun deleteClient(id: Int) {
        viewModelScope.launch {
            try {
                customerRepository.delete(id)
                loadClients()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error al eliminar cliente")
            }
        }
    }
}
