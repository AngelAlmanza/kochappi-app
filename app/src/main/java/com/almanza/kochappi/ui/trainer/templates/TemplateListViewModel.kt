package com.almanza.kochappi.ui.trainer.templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.almanza.kochappi.domain.model.Template
import com.almanza.kochappi.domain.repository.TemplateRepository
import com.almanza.kochappi.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateListViewModel @Inject constructor(
    private val templateRepository: TemplateRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Template>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<Template>>> = _uiState.asStateFlow()

    init {
        loadTemplates()
    }

    fun loadTemplates() {
        if (_uiState.value is UiState.Loading) return
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = try {
                UiState.Success(templateRepository.getTemplates())
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al cargar plantillas")
            }
        }
    }

    fun deleteTemplate(id: Int) {
        viewModelScope.launch {
            try {
                templateRepository.deleteTemplate(id)
                loadTemplates()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error al eliminar plantilla")
            }
        }
    }
}
