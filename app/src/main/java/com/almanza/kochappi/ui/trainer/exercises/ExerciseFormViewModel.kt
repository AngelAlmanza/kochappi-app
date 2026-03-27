package com.almanza.kochappi.ui.trainer.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.almanza.kochappi.domain.model.Exercise
import com.almanza.kochappi.domain.repository.ExerciseRepository
import com.almanza.kochappi.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseFormViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    private val _loadState = MutableStateFlow<UiState<Exercise>>(UiState.Idle)
    val loadState: StateFlow<UiState<Exercise>> = _loadState.asStateFlow()

    private val _saveState = MutableStateFlow<UiState<Exercise>>(UiState.Idle)
    val saveState: StateFlow<UiState<Exercise>> = _saveState.asStateFlow()

    fun loadExercise(id: Int) {
        viewModelScope.launch {
            _loadState.value = UiState.Loading
            _loadState.value = try {
                UiState.Success(exerciseRepository.getExercise(id))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al cargar ejercicio")
            }
        }
    }

    fun saveExercise(exerciseId: Int?, name: String, videoUrl: String?) {
        if (_saveState.value is UiState.Loading) return
        viewModelScope.launch {
            _saveState.value = UiState.Loading
            _saveState.value = try {
                val exercise = if (exerciseId != null) {
                    exerciseRepository.updateExercise(exerciseId, name, videoUrl)
                } else {
                    exerciseRepository.createExercise(name, videoUrl)
                }
                UiState.Success(exercise)
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al guardar ejercicio")
            }
        }
    }
}
