package com.almanza.kochappi.ui.trainer.templates

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.almanza.kochappi.domain.model.CreateDetailParams
import com.almanza.kochappi.domain.model.Exercise
import com.almanza.kochappi.domain.model.TemplateDetail
import com.almanza.kochappi.domain.model.TemplateWithDetails
import com.almanza.kochappi.domain.repository.ExerciseRepository
import com.almanza.kochappi.domain.repository.TemplateRepository
import com.almanza.kochappi.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateFormViewModel @Inject constructor(
    private val templateRepository: TemplateRepository,
    private val exerciseRepository: ExerciseRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val templateId: Int? = savedStateHandle.get<Int>("templateId")

    private val _loadState = MutableStateFlow<UiState<TemplateWithDetails>>(UiState.Idle)
    val loadState: StateFlow<UiState<TemplateWithDetails>> = _loadState.asStateFlow()

    private val _saveState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val saveState: StateFlow<UiState<Unit>> = _saveState.asStateFlow()

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    // Details managed locally before saving (for new templates or pending additions)
    private val _pendingDetails = MutableStateFlow<List<CreateDetailParams>>(emptyList())
    val pendingDetails: StateFlow<List<CreateDetailParams>> = _pendingDetails.asStateFlow()

    // Details already saved on server (for existing templates)
    private val _savedDetails = MutableStateFlow<List<TemplateDetail>>(emptyList())
    val savedDetails: StateFlow<List<TemplateDetail>> = _savedDetails.asStateFlow()

    init {
        loadExercises()
        if (templateId != null) {
            loadTemplate(templateId)
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            try {
                _exercises.value = exerciseRepository.getExercises()
            } catch (_: Exception) {
                // Exercises list is supplementary, don't block on failure
            }
        }
    }

    private fun loadTemplate(id: Int) {
        viewModelScope.launch {
            _loadState.value = UiState.Loading
            _loadState.value = try {
                val template = templateRepository.getTemplate(id)
                _savedDetails.value = template.details
                UiState.Success(template)
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al cargar plantilla")
            }
        }
    }

    fun addPendingDetail(detail: CreateDetailParams) {
        _pendingDetails.value = _pendingDetails.value + detail
    }

    fun removePendingDetail(index: Int) {
        _pendingDetails.value = _pendingDetails.value.toMutableList().apply {
            removeAt(index)
        }
    }

    fun updatePendingDetail(index: Int, detail: CreateDetailParams) {
        _pendingDetails.value = _pendingDetails.value.toMutableList().apply {
            set(index, detail)
        }
    }

    /**
     * Replaces a saved detail: deletes it from the server immediately, then queues
     * the updated values as a pending detail so they are submitted when the template is saved.
     */
    fun replaceSavedDetail(detailId: Int, detail: CreateDetailParams) {
        val tid = templateId ?: return
        viewModelScope.launch {
            try {
                templateRepository.deleteDetail(tid, detailId)
                _savedDetails.value = _savedDetails.value.filter { it.id != detailId }
                _pendingDetails.value = _pendingDetails.value + detail
            } catch (_: Exception) {
                // Could expose error via snackbar in a future iteration
            }
        }
    }

    fun deleteSavedDetail(detailId: Int) {
        val tid = templateId ?: return
        viewModelScope.launch {
            try {
                templateRepository.deleteDetail(tid, detailId)
                _savedDetails.value = _savedDetails.value.filter { it.id != detailId }
            } catch (_: Exception) {
                // Could show error via snackbar
            }
        }
    }

    fun saveTemplate(name: String, description: String?) {
        if (_saveState.value is UiState.Loading) return
        viewModelScope.launch {
            _saveState.value = UiState.Loading
            _saveState.value = try {
                if (templateId != null) {
                    // Update template metadata
                    templateRepository.updateTemplate(templateId, name, description)
                    // Add any pending details
                    for (detail in _pendingDetails.value) {
                        templateRepository.addDetail(
                            templateId = templateId,
                            exerciseId = detail.exerciseId,
                            dayOfWeek = detail.dayOfWeek,
                            displayOrder = detail.displayOrder,
                            sets = detail.sets,
                            reps = detail.reps,
                        )
                    }
                } else {
                    // Create new template with all pending details
                    templateRepository.createTemplate(
                        name = name,
                        description = description,
                        details = _pendingDetails.value,
                    )
                }
                UiState.Success(Unit)
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al guardar plantilla")
            }
        }
    }

    fun getExerciseName(exerciseId: Int): String {
        return _exercises.value.find { it.id == exerciseId }?.name ?: "Ejercicio #$exerciseId"
    }
}
