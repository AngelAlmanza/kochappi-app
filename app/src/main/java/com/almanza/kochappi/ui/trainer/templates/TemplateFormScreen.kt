package com.almanza.kochappi.ui.trainer.templates

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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

// Placeholder model for exercises within a template day
private data class TemplateExercisePreview(
    val name: String,
    val sets: Int,
    val reps: Int,
    val loadDescription: String,
    val notes: String,
)

// Sample data per day (index 0..6)
private val sampleExercisesByDay: Map<Int, List<TemplateExercisePreview>> = mapOf(
    0 to listOf(
        TemplateExercisePreview("Press de banca", 4, 10, "80 kg", "Agarre medio"),
        TemplateExercisePreview("Press inclinado", 3, 10, "60 kg", ""),
        TemplateExercisePreview("Aperturas con mancuerna", 3, 12, "16 kg", "Control en excéntrica"),
        TemplateExercisePreview("Fondos en paralelas", 3, 12, "Peso corporal", ""),
    ),
    1 to listOf(
        TemplateExercisePreview("Sentadilla", 4, 8, "75% 1RM", "Profundidad completa"),
        TemplateExercisePreview("Prensa de pierna", 4, 10, "120 kg", ""),
        TemplateExercisePreview("Extensión de cuádriceps", 3, 12, "40 kg", ""),
    ),
    2 to emptyList(),
    3 to listOf(
        TemplateExercisePreview("Peso muerto", 4, 6, "70% 1RM", "Cinturón recomendado"),
        TemplateExercisePreview("Jalón al pecho", 4, 10, "60 kg", "Agarre ancho"),
        TemplateExercisePreview("Remo con barra", 3, 10, "50 kg", ""),
    ),
    4 to listOf(
        TemplateExercisePreview("Press militar", 4, 8, "40 kg", ""),
        TemplateExercisePreview("Elevaciones laterales", 3, 15, "10 kg", ""),
    ),
    5 to emptyList(),
    6 to emptyList(),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateFormScreen(
    templateId: String?,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onAddExercise: (dayIndex: Int) -> Unit,
    onEditExercise: (dayIndex: Int, exerciseIndex: Int) -> Unit,
) {
    val isEditing = templateId != null

    var name by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var selectedDay by rememberSaveable { mutableIntStateOf(0) }

    val currentExercises = sampleExercisesByDay[selectedDay].orEmpty()

    Scaffold(
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
                        onClick = onSave,
                        enabled = name.isNotBlank(),
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
            // ── Template info ──
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
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Categoría (ej. Fuerza, Pecho, Full Body)") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Day tabs ──
            DayTabRow(
                selectedDay = selectedDay,
                onDaySelected = { selectedDay = it },
                exerciseCountByDay = sampleExercisesByDay.mapValues { it.value.size },
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
                        text = "Sin ejercicios para ${com.almanza.kochappi.ui.common.dayLabels[selectedDay]}",
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
                            exercise = ExerciseCardData(
                                name = exercise.name,
                                sets = exercise.sets,
                                reps = exercise.reps,
                                loadDescription = exercise.loadDescription,
                                notes = exercise.notes,
                            ),
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
