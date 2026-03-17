package com.almanza.kochappi.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

val dayLabels = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayTabRow(
    selectedDay: Int,
    onDaySelected: (Int) -> Unit,
    exerciseCountByDay: Map<Int, Int> = emptyMap(),
) {
    PrimaryScrollableTabRow(
        selectedTabIndex = selectedDay,
        containerColor = MaterialTheme.colorScheme.surface,
        edgePadding = 24.dp,
    ) {
        dayLabels.forEachIndexed { index, label ->
            val count = exerciseCountByDay[index] ?: 0
            Tab(
                selected = selectedDay == index,
                onClick = { onDaySelected(index) },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(label)
                        if (count > 0) {
                            Text(
                                text = "$count ej.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
            )
        }
    }
}
