# Manejo de Estado UI

## UiState

```kotlin
// ui/common/UiState.kt
sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String, val code: Int? = null) : UiState<Nothing>
}
```

## Patrón en ViewModel

Cada ViewModel expone un `StateFlow<UiState<T>>`:

```kotlin
private val _uiState = MutableStateFlow<UiState<MiModelo>>(UiState.Idle)
val uiState: StateFlow<UiState<MiModelo>> = _uiState.asStateFlow()

fun cargarDatos() {
    viewModelScope.launch {
        _uiState.value = UiState.Loading
        _uiState.value = try {
            UiState.Success(repository.obtenerDatos())
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Error desconocido")
        }
    }
}
```

## Patrón en Composable

```kotlin
val uiState by viewModel.uiState.collectAsStateWithLifecycle()

when (uiState) {
    is UiState.Idle    -> { /* nada */ }
    is UiState.Loading -> CircularProgressIndicator()
    is UiState.Success -> ContenidoPrincipal((uiState as UiState.Success).data)
    is UiState.Error   -> ErrorView((uiState as UiState.Error).message)
}
```
