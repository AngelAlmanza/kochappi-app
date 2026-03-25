package com.almanza.kochappi.ui.auth.forgot

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.almanza.kochappi.ui.common.OtpTextField
import com.almanza.kochappi.ui.common.UiState

private const val STEP_EMAIL = 0
private const val STEP_CODE = 1
private const val STEP_NEW_PASSWORD = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onPasswordReset: () -> Unit,
    onBack: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
) {
    var currentStep by rememberSaveable { mutableIntStateOf(STEP_EMAIL) }
    var email by rememberSaveable { mutableStateOf("") }
    var code by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var newPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val sendCodeState by viewModel.sendCodeState.collectAsStateWithLifecycle()
    val resetPasswordState by viewModel.resetPasswordState.collectAsStateWithLifecycle()

    LaunchedEffect(sendCodeState) {
        when (val state = sendCodeState) {
            is UiState.Success -> currentStep = STEP_CODE
            is UiState.Error -> snackbarHostState.showSnackbar(state.message)
            else -> {}
        }
    }

    LaunchedEffect(resetPasswordState) {
        when (val state = resetPasswordState) {
            is UiState.Success -> onPasswordReset()
            is UiState.Error -> snackbarHostState.showSnackbar(state.message)
            else -> {}
        }
    }

    val stepTitle = when (currentStep) {
        STEP_EMAIL -> "Recuperar contraseña"
        STEP_CODE -> "Verificar código"
        else -> "Nueva contraseña"
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stepTitle) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep > STEP_EMAIL) {
                            currentStep--
                        } else {
                            onBack()
                        }
                    }) {
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
        AnimatedContent(
            targetState = currentStep,
            label = "forgot_password_step",
        ) { step ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                when (step) {
                    STEP_EMAIL -> EmailStep(
                        email = email,
                        onEmailChange = { email = it },
                        isLoading = sendCodeState is UiState.Loading,
                        onSendCode = { viewModel.sendCode(email) },
                    )
                    STEP_CODE -> CodeStep(
                        code = code,
                        onCodeChange = { code = it },
                        onVerify = { currentStep = STEP_NEW_PASSWORD },
                    )
                    STEP_NEW_PASSWORD -> NewPasswordStep(
                        newPassword = newPassword,
                        onNewPasswordChange = { newPassword = it },
                        confirmPassword = confirmPassword,
                        onConfirmPasswordChange = { confirmPassword = it },
                        newPasswordVisible = newPasswordVisible,
                        onToggleNewPasswordVisibility = {
                            newPasswordVisible = !newPasswordVisible
                        },
                        confirmPasswordVisible = confirmPasswordVisible,
                        onToggleConfirmPasswordVisibility = {
                            confirmPasswordVisible = !confirmPasswordVisible
                        },
                        isLoading = resetPasswordState is UiState.Loading,
                        onResetPassword = {
                            viewModel.resetPassword(email, code, newPassword)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun EmailStep(
    email: String,
    onEmailChange: (String) -> Unit,
    isLoading: Boolean,
    onSendCode: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Text(
        text = "Ingresa tu correo electrónico y te enviaremos un código de verificación.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(32.dp))

    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Correo electrónico") },
        singleLine = true,
        enabled = !isLoading,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                if (email.isNotBlank()) onSendCode()
            },
        ),
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(Modifier.height(24.dp))

    Button(
        onClick = onSendCode,
        enabled = email.isNotBlank() && !isLoading,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = "Enviar código",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun CodeStep(
    code: String,
    onCodeChange: (String) -> Unit,
    onVerify: () -> Unit,
) {
    Text(
        text = "Ingresa el código de verificación que enviamos a tu correo.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(32.dp))

    OtpTextField(
        value = code,
        onValueChange = onCodeChange,
        digitCount = 6,
        onComplete = { onVerify() },
    )

    Spacer(Modifier.height(24.dp))

    Button(
        onClick = onVerify,
        enabled = code.length == 6,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
    ) {
        Text(
            text = "Verificar",
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun NewPasswordStep(
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    newPasswordVisible: Boolean,
    onToggleNewPasswordVisibility: () -> Unit,
    confirmPasswordVisible: Boolean,
    onToggleConfirmPasswordVisibility: () -> Unit,
    isLoading: Boolean,
    onResetPassword: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val passwordsMatch = newPassword == confirmPassword && newPassword.isNotBlank()

    Text(
        text = "Ingresa tu nueva contraseña.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(32.dp))

    OutlinedTextField(
        value = newPassword,
        onValueChange = onNewPasswordChange,
        label = { Text("Nueva contraseña") },
        singleLine = true,
        enabled = !isLoading,
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (newPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = onToggleNewPasswordVisibility) {
                Icon(
                    imageVector = if (newPasswordVisible) {
                        Icons.Default.VisibilityOff
                    } else {
                        Icons.Default.Visibility
                    },
                    contentDescription = if (newPasswordVisible) {
                        "Ocultar contraseña"
                    } else {
                        "Mostrar contraseña"
                    },
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
        ),
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(Modifier.height(16.dp))

    OutlinedTextField(
        value = confirmPassword,
        onValueChange = onConfirmPasswordChange,
        label = { Text("Confirmar contraseña") },
        singleLine = true,
        enabled = !isLoading,
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (confirmPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = onToggleConfirmPasswordVisibility) {
                Icon(
                    imageVector = if (confirmPasswordVisible) {
                        Icons.Default.VisibilityOff
                    } else {
                        Icons.Default.Visibility
                    },
                    contentDescription = if (confirmPasswordVisible) {
                        "Ocultar contraseña"
                    } else {
                        "Mostrar contraseña"
                    },
                )
            }
        },
        isError = confirmPassword.isNotBlank() && !passwordsMatch,
        supportingText = if (confirmPassword.isNotBlank() && !passwordsMatch) {
            { Text("Las contraseñas no coinciden") }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                if (passwordsMatch) onResetPassword()
            },
        ),
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(Modifier.height(24.dp))

    Button(
        onClick = onResetPassword,
        enabled = passwordsMatch && !isLoading,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = "Restablecer contraseña",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}
