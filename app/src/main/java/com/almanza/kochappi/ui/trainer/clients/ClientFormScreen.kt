package com.almanza.kochappi.ui.trainer.clients

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.almanza.kochappi.domain.model.User
import com.almanza.kochappi.ui.common.UiState
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientFormScreen(
    clientId: Int?,
    onSave: () -> Unit,
    onBack: () -> Unit,
    viewModel: ClientFormViewModel = hiltViewModel(),
) {
    val isEditing = clientId != null
    val saveState by viewModel.saveState.collectAsState()
    val usersState by viewModel.users.collectAsState()
    val customerState by viewModel.customer.collectAsState()

    var name by rememberSaveable { mutableStateOf("") }
    var birthdate by rememberSaveable { mutableStateOf("") }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // User selection / registration fields
    var createNewUser by rememberSaveable { mutableStateOf(false) }
    var selectedUser by rememberSaveable { mutableStateOf<Int?>(null) }
    var userEmail by rememberSaveable { mutableStateOf("") }
    var userPassword by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (isEditing) {
            viewModel.loadClient(clientId)
        } else {
            viewModel.loadUsers()
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        birthdate = Instant.ofEpochMilli(millis)
                            .atOffset(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ISO_LOCAL_DATE)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Pre-fill form when editing
    LaunchedEffect(customerState) {
        if (customerState is UiState.Success) {
            val customer = (customerState as UiState.Success).data
            name = customer.name
            birthdate = customer.birthdate
        }
    }

    // Navigate on save success
    LaunchedEffect(saveState) {
        if (saveState is UiState.Success) {
            viewModel.resetSaveState()
            onSave()
        }
    }

    // Auto-switch to create new user if no users available
    LaunchedEffect(usersState) {
        if (usersState is UiState.Success) {
            val users = (usersState as UiState.Success).data
            if (users.isEmpty()) {
                createNewUser = true
            }
        }
    }

    val isFormValid = if (isEditing) {
        name.isNotBlank() && birthdate.isNotBlank()
    } else if (createNewUser) {
        name.isNotBlank() && birthdate.isNotBlank() && userEmail.isNotBlank() && userPassword.isNotBlank()
    } else {
        name.isNotBlank() && birthdate.isNotBlank() && selectedUser != null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEditing) "Editar Cliente" else "Nuevo Cliente")
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
                text = "Información del cliente",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo *") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = birthdate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de nacimiento *") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
            )

            // User section — only shown when creating
            if (!isEditing) {
                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Cuenta de usuario",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )

                Spacer(Modifier.height(8.dp))

                when (val state = usersState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is UiState.Success -> {
                        val users = state.data
                        if (users.isNotEmpty()) {
                            // Toggle: existing user or new
                            Row (
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                            ) {
                                Checkbox(
                                    checked = createNewUser,
                                    onCheckedChange = {
                                        createNewUser = it
                                        if (it) selectedUser = null
                                    },
                                )
                                Text(
                                    text = "Crear nuevo usuario",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }

                            if (!createNewUser) {
                                Spacer(Modifier.height(8.dp))
                                UserDropdown(
                                    users = users,
                                    selectedUserId = selectedUser,
                                    onUserSelected = { selectedUser = it },
                                )
                            }
                        }

                        if (createNewUser || users.isEmpty()) {
                            Spacer(Modifier.height(8.dp))
                            NewUserFields(
                                email = userEmail,
                                onEmailChange = { userEmail = it },
                                password = userPassword,
                                onPasswordChange = { userPassword = it },
                            )
                        }
                    }

                    is UiState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }

                    is UiState.Idle -> {}
                }
            }

            // Error from save
            if (saveState is UiState.Error) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = (saveState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    if (isEditing) {
                        viewModel.updateClient(clientId, name, birthdate)
                    } else if (createNewUser) {
                        viewModel.registerAndCreateClient(userEmail, userPassword, name, birthdate)
                    } else {
                        viewModel.createClient(selectedUser!!, name, birthdate)
                    }
                },
                enabled = isFormValid && saveState !is UiState.Loading,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            ) {
                if (saveState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = if (isEditing) "Guardar cambios" else "Registrar cliente",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserDropdown(
    users: List<User>,
    selectedUserId: Int?,
    onUserSelected: (Int) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val selectedUser = users.find { it.id == selectedUserId }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = selectedUser?.let { "${it.name} (${it.email})" } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Seleccionar usuario") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            users.forEach { user ->
                DropdownMenuItem(
                    text = { Text("${user.name} (${user.email})") },
                    onClick = {
                        onUserSelected(user.id)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun NewUserFields(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Correo electrónico *") },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
        ),
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(Modifier.height(16.dp))

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Contraseña *") },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        ),
        modifier = Modifier.fillMaxWidth(),
    )
}
