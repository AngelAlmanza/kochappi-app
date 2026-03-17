package com.almanza.kochappi.ui.trainer.routines

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
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
import androidx.compose.ui.unit.dp
import com.almanza.kochappi.ui.common.DayTabRow
import com.almanza.kochappi.ui.common.ExerciseCard
import com.almanza.kochappi.ui.common.ExerciseCardData
import com.almanza.kochappi.ui.common.dayLabels

// Placeholder data
private data class ClientOption(val id: String, val name: String)

private val sampleClients = listOf(
    ClientOption("1", "Carlos García"),
    ClientOption("2", "María López"),
    ClientOption("3", "Juan Martínez"),
    ClientOption("4", "Ana Rodríguez"),
    ClientOption("5", "Pedro Sánchez"),
)

private val sampleExercisesByDay: Map<Int, List<ExerciseCardData>> = mapOf(
    0 to listOf(
        ExerciseCardData("Press de banca", 4, 10, "80 kg", "Agarre medio"),
        ExerciseCardData("Press inclinado", 3, 10, "60 kg", ""),
        ExerciseCardData("Aperturas con mancuerna", 3, 12, "16 kg", "Control en excéntrica"),
    ),
    1 to listOf(
        ExerciseCardData("Sentadilla", 4, 8, "100 kg", "Profundidad completa"),
        ExerciseCardData("Prensa de pierna", 4, 10, "120 kg", ""),
    ),
    2 to emptyList(),
    3 to listOf(
        ExerciseCardData("Peso muerto", 4, 6, "70% 1RM", "Cinturón recomendado"),
        ExerciseCardData("Jalón al pecho", 4, 10, "60 kg", "Agarre ancho"),
    ),
    4 to emptyList(),
    5 to emptyList(),
    6 to emptyList(),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineFormScreen(
    routineId: String?,
    clientId: String,
    templateId: String,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onSelectTemplate: (clientId: String) -> Unit,
    onAddExercise: (dayIndex: Int) -> Unit,
    onEditExercise: (dayIndex: Int, exerciseIndex: Int) -> Unit,
) {
    val isEditing = routineId != null
    val hasPreselectedClient = clientId.isNotEmpty()
    val loadedFromTemplate = templateId.isNotEmpty()

    var name by rememberSaveable { mutableStateOf("") }
    var selectedDay by rememberSaveable { mutableIntStateOf(0) }

    // Client selection state
    var clientDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var selectedClientId by rememberSaveable {
        mutableStateOf(if (hasPreselectedClient) clientId else "")
    }
    val selectedClientName = sampleClients.find { it.id == selectedClientId }?.name ?: ""

    val currentExercises = if (loadedFromTemplate || isEditing) {
        sampleExercisesByDay[selectedDay].orEmpty()
    } else {
        emptyList()
    }

    val isFormValid = name.isNotBlank() && selectedClientId.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEditing) "Editar Rutina" else "Nueva Rutina")
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
                        onClick = onSave,
                        enabled = isFormValid,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Guardar",
                            tint = if (isFormValid) {
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
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(Modifier.height(8.dp))

                // ── Client selector ──
                Text(
                    text = "Cliente",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )

                Spacer(Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = clientDropdownExpanded,
                    onExpandedChange = {
                        if (!hasPreselectedClient) clientDropdownExpanded = it
                    },
                ) {
                    OutlinedTextField(
                        value = selectedClientName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Seleccionar cliente *") },
                        trailingIcon = {
                            if (!hasPreselectedClient) {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = clientDropdownExpanded)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                    )

                    if (!hasPreselectedClient) {
                        ExposedDropdownMenu(
                            expanded = clientDropdownExpanded,
                            onDismissRequest = { clientDropdownExpanded = false },
                        ) {
                            sampleClients.forEach { client ->
                                DropdownMenuItem(
                                    text = { Text(client.name) },
                                    onClick = {
                                        selectedClientId = client.id
                                        clientDropdownExpanded = false
                                    },
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Routine name ──
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la rutina *") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth(),
                )

                // ── Load from template button ──
                if (!isEditing && !loadedFromTemplate) {
                    Spacer(Modifier.height(12.dp))
                    LoadFromTemplateCard(
                        onClick = {
                            val cId = selectedClientId.ifEmpty { clientId }
                            onSelectTemplate(cId)
                        },
                    )
                }

                if (loadedFromTemplate) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Cargada desde plantilla",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Day tabs ──
            DayTabRow(
                selectedDay = selectedDay,
                onDaySelected = { selectedDay = it },
                exerciseCountByDay = if (loadedFromTemplate || isEditing) {
                    sampleExercisesByDay.mapValues { it.value.size }
                } else {
                    emptyMap()
                },
            )

            Spacer(Modifier.height(12.dp))

            // ── Exercises for selected day ──
            if (currentExercises.isEmpty()) {
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
                    FilledTonalButton(onClick = { onAddExercise(selectedDay) }) {
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
                    itemsIndexed(currentExercises) { index, exercise ->
                        ExerciseCard(
                            index = index,
                            exercise = exercise,
                            onEdit = { onEditExercise(selectedDay, index) },
                            onDelete = { /* no-op UI only */ },
                        )
                    }

                    item {
                        FilledTonalButton(
                            onClick = { onAddExercise(selectedDay) },
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
}

@Composable
private fun LoadFromTemplateCard(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Cargar desde plantilla",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                Text(
                    text = "Usa una plantilla existente como base",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f),
                )
            }
        }
    }
}
