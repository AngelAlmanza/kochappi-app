package com.almanza.kochappi.ui.trainer.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.almanza.kochappi.ui.common.UiState
import com.almanza.kochappi.ui.common.dayLabels

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateExerciseFormScreen(
    dayIndex: Int,
    /** Pre-assigned display order (auto-computed by caller). Read-only in the form. */
    initialDisplayOrder: Int,
    onSave: (exerciseId: Int, dayOfWeek: Int, displayOrder: Int, sets: Int, reps: Int) -> Unit,
    onBack: () -> Unit,
    initialExerciseId: Int = -1,
    initialSets: Int? = null,
    initialReps: Int? = null,
    viewModel: TemplateExerciseFormViewModel = hiltViewModel(),
) {
    val isEditing = initialExerciseId > 0
    val dayName = dayLabels.getOrElse(dayIndex) { "Día" }
    val exercisesState by viewModel.exercisesState.collectAsStateWithLifecycle()

    var selectedExerciseId by rememberSaveable { mutableIntStateOf(initialExerciseId) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var sets by rememberSaveable { mutableStateOf(initialSets?.toString() ?: "") }
    var reps by rememberSaveable { mutableStateOf(initialReps?.toString() ?: "") }

    val exercises = (exercisesState as? UiState.Success)?.data.orEmpty()
    val selectedExerciseName = exercises.find { it.id == selectedExerciseId }?.name ?: ""
    val isFormValid = selectedExerciseId > 0
            && sets.toIntOrNull() != null
            && reps.toIntOrNull() != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar ejercicio — $dayName" else "Agregar ejercicio — $dayName") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { innerPadding ->
        when (exercisesState) {
            is UiState.Idle, is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = (exercisesState as UiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            is UiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 24.dp)
                        .imePadding()
                        .verticalScroll(rememberScrollState()),
                ) {
                    Spacer(Modifier.height(8.dp))

                    // Exercise selector
                    Text(
                        text = "Ejercicio",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Spacer(Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                    ) {
                        OutlinedTextField(
                            value = selectedExerciseName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Seleccionar ejercicio *") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            exercises.forEach { exercise ->
                                DropdownMenuItem(
                                    text = { Text(exercise.name) },
                                    onClick = {
                                        selectedExerciseId = exercise.id
                                        expanded = false
                                    },
                                )
                            }
                            if (exercises.isEmpty()) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "No hay ejercicios disponibles",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    },
                                    onClick = { expanded = false },
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Sets, Reps, Order
                    Text(
                        text = "Configuración",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = sets,
                            onValueChange = { sets = it.filter { c -> c.isDigit() } },
                            label = { Text("Series *") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next,
                            ),
                            modifier = Modifier.weight(1f),
                        )

                        Spacer(Modifier.width(12.dp))

                        OutlinedTextField(
                            value = reps,
                            onValueChange = { reps = it.filter { c -> c.isDigit() } },
                            label = { Text("Reps *") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done,
                            ),
                            modifier = Modifier.weight(1f),
                        )

                        Spacer(Modifier.width(12.dp))

                        // Order is auto-assigned and read-only; reordering will be a future feature
                        OutlinedTextField(
                            value = initialDisplayOrder.toString(),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Orden") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    Button(
                        onClick = {
                            // dayIndex is 0-based (tab index); API expects 1-based dayOfWeek
                            onSave(
                                selectedExerciseId,
                                dayIndex + 1,
                                initialDisplayOrder,
                                sets.toInt(),
                                reps.toInt(),
                            )
                        },
                        enabled = isFormValid,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                    ) {
                        Text(
                            text = if (isEditing) "Guardar cambios" else "Agregar ejercicio",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}
