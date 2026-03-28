package com.almanza.kochappi.ui.trainer.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.almanza.kochappi.domain.model.TemplateDetail
import com.almanza.kochappi.ui.common.DayTabRow
import com.almanza.kochappi.ui.common.ExerciseCard
import com.almanza.kochappi.ui.common.ExerciseCardData
import com.almanza.kochappi.ui.common.UiState
import com.almanza.kochappi.ui.common.dayLabels

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateFormScreen(
    templateId: Int?,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onAddExercise: (dayIndex: Int, nextDisplayOrder: Int) -> Unit,
    onEditExercise: (detailId: Int, pendingIndex: Int, dayIndex: Int, exerciseId: Int, sets: Int, reps: Int, displayOrder: Int) -> Unit,
    viewModel: TemplateFormViewModel = hiltViewModel(),
) {
    val isEditing = templateId != null

    val loadState by viewModel.loadState.collectAsStateWithLifecycle()
    val saveState by viewModel.saveState.collectAsStateWithLifecycle()
    val pendingDetails by viewModel.pendingDetails.collectAsStateWithLifecycle()
    val savedDetails by viewModel.savedDetails.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var selectedDay by rememberSaveable { mutableIntStateOf(0) }
    var initialized by rememberSaveable { mutableStateOf(false) }
    // (detailId > 0 for saved, -1 for pending) to (pendingIndex >= 0 for pending, -1 for saved)
    var detailToDelete by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    // Populate fields when editing and data loads
    LaunchedEffect(loadState) {
        if (loadState is UiState.Success && !initialized) {
            val template = (loadState as UiState.Success).data
            name = template.name
            description = template.description ?: ""
            initialized = true
        }
    }

    // Handle save result
    LaunchedEffect(saveState) {
        when (saveState) {
            is UiState.Success -> onSave()
            is UiState.Error -> {
                snackbarHostState.showSnackbar((saveState as UiState.Error).message)
            }
            else -> {}
        }
    }

    // selectedDay is a 0-based tab index; dayOfWeek stored in details is 1-based (1=Monday)
    val selectedDayOfWeek = selectedDay + 1

    // Combine saved and pending details for display.
    // Pending details use negative IDs to encode their global index in _pendingDetails:
    //   id = -(globalIndex + 1)  →  globalIndex = -(id + 1)
    val allDetailsForDay = savedDetails.filter { it.dayOfWeek == selectedDayOfWeek } +
        pendingDetails.mapIndexedNotNull { globalIndex, detail ->
            if (detail.dayOfWeek == selectedDayOfWeek) {
                TemplateDetail(
                    id = -(globalIndex + 1),
                    exerciseId = detail.exerciseId,
                    dayOfWeek = detail.dayOfWeek,
                    displayOrder = detail.displayOrder,
                    sets = detail.sets,
                    reps = detail.reps,
                )
            } else null
        }

    // Exercise count by day for the tab badges (day is 0-based tab index, convert to 1-based for comparison)
    val exerciseCountByDay = (0..6).associateWith { day ->
        val dayOfWeek = day + 1
        savedDetails.count { it.dayOfWeek == dayOfWeek } +
            pendingDetails.count { it.dayOfWeek == dayOfWeek }
    }

    // Show loading spinner while loading existing template
    if (isEditing && loadState is UiState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEditing) "Editar Plantilla" else "Nueva Plantilla")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.saveTemplate(
                                name = name,
                                description = description.ifBlank { null },
                            )
                        },
                        enabled = name.isNotBlank() && saveState !is UiState.Loading,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Guardar",
                            tint = if (name.isNotBlank()) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outlineVariant
                            },
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            // Template info fields
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la plantilla *") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(Modifier.height(20.dp))

            // Day tabs
            DayTabRow(
                selectedDay = selectedDay,
                onDaySelected = { selectedDay = it },
                exerciseCountByDay = exerciseCountByDay,
            )

            Spacer(Modifier.height(12.dp))

            // Exercises for selected day
            if (allDetailsForDay.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.outlineVariant,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Sin ejercicios para ${dayLabels[selectedDay]}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(16.dp))
                    FilledTonalButton(onClick = { onAddExercise(selectedDay, allDetailsForDay.size + 1) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Agregar ejercicio")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(allDetailsForDay) { index, detail ->
                        ExerciseCard(
                            index = index,
                            exercise = ExerciseCardData(
                                name = viewModel.getExerciseName(detail.exerciseId),
                                sets = detail.sets,
                                reps = detail.reps,
                                loadDescription = "",
                                notes = "",
                            ),
                            onEdit = {
                                val detailId = if (detail.id > 0) detail.id else -1
                                val pendingIndex = if (detail.id < 0) -(detail.id + 1) else -1
                                onEditExercise(
                                    detailId,
                                    pendingIndex,
                                    selectedDay,
                                    detail.exerciseId,
                                    detail.sets,
                                    detail.reps,
                                    detail.displayOrder,
                                )
                            },
                            onDelete = {
                                val detailId = if (detail.id > 0) detail.id else -1
                                val pendingIndex = if (detail.id < 0) -(detail.id + 1) else -1
                                detailToDelete = Pair(detailId, pendingIndex)
                            },
                            showActions = true,
                        )
                    }

                    item {
                        FilledTonalButton(
                            onClick = { onAddExercise(selectedDay, allDetailsForDay.size + 1) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Agregar ejercicio")
                        }
                    }
                }
            }
        }
    }

    if (detailToDelete != null) {
        AlertDialog(
            onDismissRequest = { detailToDelete = null },
            title = { Text("Eliminar ejercicio") },
            text = { Text("¿Estás seguro de que quieres eliminar este ejercicio de la plantilla?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        detailToDelete?.let { (detailId, pendingIndex) ->
                            if (detailId > 0) {
                                viewModel.deleteSavedDetail(detailId)
                            } else {
                                viewModel.removePendingDetail(pendingIndex)
                            }
                        }
                        detailToDelete = null
                    },
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { detailToDelete = null }) {
                    Text("Cancelar")
                }
            },
        )
    }
}
