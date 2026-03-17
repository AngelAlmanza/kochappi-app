package com.almanza.kochappi.ui.trainer.routines

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.almanza.kochappi.ui.common.DayTabRow
import com.almanza.kochappi.ui.common.ExerciseCard
import com.almanza.kochappi.ui.common.ExerciseCardData
import com.almanza.kochappi.ui.common.dayLabels

// Placeholder data
private data class RoutineDetailData(
    val id: String,
    val name: String,
    val clientName: String,
    val isActive: Boolean,
    val startDate: String,
)

private val sampleRoutine = RoutineDetailData(
    id = "r1",
    name = "Hipertrofia Fase 2",
    clientName = "Carlos García",
    isActive = true,
    startDate = "01/03/2026",
)

private val sampleExercisesByDay: Map<Int, List<ExerciseCardData>> = mapOf(
    0 to listOf(
        ExerciseCardData("Press de banca", 4, 10, "80 kg", "Agarre medio"),
        ExerciseCardData("Press inclinado", 3, 10, "60 kg", ""),
        ExerciseCardData("Aperturas con mancuerna", 3, 12, "16 kg", "Control en excéntrica"),
        ExerciseCardData("Fondos en paralelas", 3, 12, "Peso corporal", ""),
    ),
    1 to listOf(
        ExerciseCardData("Sentadilla", 4, 8, "100 kg", "Profundidad completa"),
        ExerciseCardData("Prensa de pierna", 4, 10, "120 kg", ""),
        ExerciseCardData("Extensión de cuádriceps", 3, 12, "40 kg", ""),
    ),
    2 to emptyList(),
    3 to listOf(
        ExerciseCardData("Peso muerto", 4, 6, "70% 1RM", "Cinturón recomendado"),
        ExerciseCardData("Jalón al pecho", 4, 10, "60 kg", "Agarre ancho"),
        ExerciseCardData("Remo con barra", 3, 10, "50 kg", ""),
    ),
    4 to listOf(
        ExerciseCardData("Press militar", 4, 8, "40 kg", ""),
        ExerciseCardData("Elevaciones laterales", 3, 15, "10 kg", ""),
    ),
    5 to emptyList(),
    6 to emptyList(),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailScreen(
    routineId: String,
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
) {
    var selectedDay by rememberSaveable { mutableIntStateOf(0) }
    val currentExercises = sampleExercisesByDay[selectedDay].orEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(sampleRoutine.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onEdit(routineId) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar rutina",
                            tint = MaterialTheme.colorScheme.primary,
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
                .padding(innerPadding)
                .padding(top = 16.dp),
        ) {
            // ── Routine info ──
            RoutineInfoCard(
                routine = sampleRoutine,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(16.dp))

            // ── Day tabs ──
            DayTabRow(
                selectedDay = selectedDay,
                onDaySelected = { selectedDay = it },
                exerciseCountByDay = sampleExercisesByDay.mapValues { it.value.size },
            )

            Spacer(Modifier.height(12.dp))

            // ── Exercises for selected day (read-only) ──
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
                        text = "Descanso — ${dayLabels[selectedDay]}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
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
                            onEdit = {},
                            onDelete = {},
                            showActions = false,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RoutineInfoCard(
    routine: RoutineDetailData,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = routine.clientName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                if (routine.isActive) {
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = "Activa",
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            iconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Desde ${routine.startDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
