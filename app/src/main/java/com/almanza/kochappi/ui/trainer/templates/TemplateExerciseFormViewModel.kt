package com.almanza.kochappi.ui.trainer.templates

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
class TemplateExerciseFormViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    private val _exercisesState = MutableStateFlow<UiState<List<Exercise>>>(UiState.Idle)
    val exercisesState: StateFlow<UiState<List<Exercise>>> = _exercisesState.asStateFlow()

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            _exercisesState.value = UiState.Loading
            _exercisesState.value = try {
                UiState.Success(exerciseRepository.getExercises())
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Error al cargar ejercicios")
            }
        }
    }
}
