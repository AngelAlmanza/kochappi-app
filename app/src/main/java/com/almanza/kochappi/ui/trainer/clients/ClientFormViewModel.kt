package com.almanza.kochappi.ui.trainer.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.almanza.kochappi.domain.model.Customer
import com.almanza.kochappi.domain.model.User
import com.almanza.kochappi.domain.model.UserRole
import com.almanza.kochappi.domain.repository.CustomerRepository
import com.almanza.kochappi.domain.repository.UserRepository
import com.almanza.kochappi.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientFormViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _users = MutableStateFlow<UiState<List<User>>>(UiState.Idle)
    val users: StateFlow<UiState<List<User>>> = _users.asStateFlow()

    private val _saveState = MutableStateFlow<UiState<Customer>>(UiState.Idle)
    val saveState: StateFlow<UiState<Customer>> = _saveState.asStateFlow()

    private val _customer = MutableStateFlow<UiState<Customer>>(UiState.Idle)
    val customer: StateFlow<UiState<Customer>> = _customer.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _users.value = UiState.Loading
            _users.value = try {
                UiState.Success(userRepository.getByRole(UserRole.CLIENT.name.lowercase(), false))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al cargar usuarios")
            }
        }
    }

    fun loadClient(id: Int) {
        viewModelScope.launch {
            _customer.value = UiState.Loading
            _customer.value = try {
                UiState.Success(customerRepository.getById(id))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al cargar cliente")
            }
        }
    }

    fun registerAndCreateClient(
        email: String,
        password: String,
        customerName: String,
        birthdate: String,
    ) {
        if (_saveState.value is UiState.Loading) return
        viewModelScope.launch {
            _saveState.value = UiState.Loading
            _saveState.value = try {
                val user = userRepository.create(customerName, email, password, UserRole.CLIENT.name.lowercase())
                UiState.Success(customerRepository.create(user.id, customerName, birthdate))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al registrar cliente")
            }
        }
    }

    fun createClient(userId: Int, name: String, birthdate: String) {
        if (_saveState.value is UiState.Loading) return
        viewModelScope.launch {
            _saveState.value = UiState.Loading
            _saveState.value = try {
                UiState.Success(customerRepository.create(userId, name, birthdate))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al crear cliente")
            }
        }
    }

    fun updateClient(id: Int, name: String, birthdate: String) {
        if (_saveState.value is UiState.Loading) return
        viewModelScope.launch {
            _saveState.value = UiState.Loading
            _saveState.value = try {
                UiState.Success(customerRepository.update(id, name, birthdate))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al actualizar cliente")
            }
        }
    }

    fun resetSaveState() {
        _saveState.value = UiState.Idle
    }
}
