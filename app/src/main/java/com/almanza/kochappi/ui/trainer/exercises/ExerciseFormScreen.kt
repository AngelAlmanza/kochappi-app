package com.almanza.kochappi.ui.trainer.exercises

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.almanza.kochappi.ui.common.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseFormScreen(
    exerciseId: Int?,
    onSave: () -> Unit,
    onBack: () -> Unit,
    viewModel: ExerciseFormViewModel = hiltViewModel(),
) {
    val isEditing = exerciseId != null
    val loadState by viewModel.loadState.collectAsStateWithLifecycle()
    val saveState by viewModel.saveState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var name by rememberSaveable { mutableStateOf("") }
    var videoUrl by rememberSaveable { mutableStateOf("") }
    var formPopulated by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(exerciseId) {
        if (exerciseId != null) viewModel.loadExercise(exerciseId)
    }

    // Populate form when exercise data loads in edit mode
    LaunchedEffect(loadState) {
        if (loadState is UiState.Success && !formPopulated) {
            val exercise = (loadState as UiState.Success).data
            name = exercise.name
            videoUrl = exercise.videoUrl ?: ""
            formPopulated = true
        }
    }

    // Navigate back on successful save
    LaunchedEffect(saveState) {
        when (saveState) {
            is UiState.Success -> onSave()
            is UiState.Error -> snackbarHostState.showSnackbar(
                (saveState as UiState.Error).message,
            )
            else -> {}
        }
    }

    val isFormValid = name.isNotBlank()
    val isSaving = saveState is UiState.Loading

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEditing) "Editar Ejercicio" else "Nuevo Ejercicio")
                },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Información básica",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del ejercicio *") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Video de referencia",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = videoUrl,
                onValueChange = { videoUrl = it },
                label = { Text("URL del video (YouTube, etc.)") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Done,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.saveExercise(
                        exerciseId = exerciseId,
                        name = name.trim(),
                        videoUrl = videoUrl.trim().ifBlank { null },
                    )
                },
                enabled = isFormValid && !isSaving,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.height(24.dp),
                    )
                } else {
                    Text(
                        text = if (isEditing) "Guardar cambios" else "Crear ejercicio",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
