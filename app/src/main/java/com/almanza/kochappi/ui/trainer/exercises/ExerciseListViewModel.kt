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
class ExerciseListViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Exercise>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<Exercise>>> = _uiState.asStateFlow()

    init {
        loadExercises()
    }

    fun loadExercises() {
        if (_uiState.value is UiState.Loading) return
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = try {
                UiState.Success(exerciseRepository.getExercises())
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al cargar ejercicios")
            }
        }
    }

    fun deleteExercise(id: Int) {
        viewModelScope.launch {
            try {
                exerciseRepository.deleteExercise(id)
                loadExercises()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error al eliminar ejercicio")
            }
        }
    }
}
