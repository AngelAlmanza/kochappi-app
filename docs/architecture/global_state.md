# Estado Global

Solo tres cosas justifican ser estado global:
1. **Sesión** — token, rol, datos básicos del perfil.
2. **Conectividad** — si no hay red, múltiples pantallas reaccionan.
3. **Snackbars/Eventos globales** — mensajes disparados desde cualquier capa.

> Regla rápida: si al cambiar ese estado *todas* las pantallas potencialmente reaccionan → es global.

---

## SessionManager

```kotlin
// domain/model/Session.kt
data class Session(
    val token: String,
    val userId: String,
    val role: UserRole,
    val displayName: String
)

enum class UserRole { TRAINER, CLIENT }
```

```kotlin
// data/session/SessionManager.kt
@Singleton
class SessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val _session = MutableStateFlow<Session?>(null)
    val session: StateFlow<Session?> = _session.asStateFlow()

    val isLoggedIn: Boolean get() = _session.value != null
    val role: UserRole?     get() = _session.value?.role

    suspend fun login(session: Session) {
        _session.value = session
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY]   = session.token
            prefs[ROLE_KEY]    = session.role.name
            prefs[USER_ID_KEY] = session.userId
        }
    }

    suspend fun logout() {
        _session.value = null
        dataStore.edit { it.clear() }
    }

    suspend fun restore() {           // llamar al arrancar la app
        val prefs = dataStore.data.first()
        val token = prefs[TOKEN_KEY] ?: return
        _session.value = Session(
            token       = token,
            userId      = prefs[USER_ID_KEY] ?: return,
            role        = UserRole.valueOf(prefs[ROLE_KEY] ?: return),
            displayName = prefs[DISPLAY_NAME_KEY] ?: ""
        )
    }
}
```

---

## GlobalEventBus

Para eventos one-shot (snackbars, navegación forzada):

```kotlin
@Singleton
class GlobalEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<GlobalEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<GlobalEvent> = _events.asSharedFlow()

    fun emit(event: GlobalEvent) { _events.tryEmit(event) }
}

sealed interface GlobalEvent {
    data class ShowSnackbar(val message: String) : GlobalEvent
    data object SessionExpired : GlobalEvent
    data object NetworkLost    : GlobalEvent
}
```

---

## RootApp — conexión en el composable raíz

```kotlin
@Composable
fun RootApp(sessionManager: SessionManager, globalEventBus: GlobalEventBus) {
    val session by sessionManager.session.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        globalEventBus.events.collect { event ->
            when (event) {
                is GlobalEvent.ShowSnackbar ->
                    snackbarHostState.showSnackbar(event.message)
                is GlobalEvent.SessionExpired ->
                    navController.navigate(Route.Login.path) {
                        popUpTo(0) { inclusive = true }
                    }
                else -> {}
            }
        }
    }

    // NavHost con startDestination derivado de session/rol ...
}
```
