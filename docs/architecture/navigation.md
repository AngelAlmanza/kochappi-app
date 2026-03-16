# Navegación por Rol

## Rutas

```kotlin
sealed class Route(val path: String) {
    data object Login           : Route("login")
    data object TrainerDashboard: Route("trainer/dashboard")
    data object TrainerClients  : Route("trainer/clients")
    data object ClientDashboard : Route("client/dashboard")
    data object ClientProgress  : Route("client/progress")
}
```

## Lógica de arranque

Al iniciar la app se restaura la sesión desde DataStore. El `startDestination` del NavHost se determina según el rol:

```kotlin
val startDestination = when {
    session == null                        -> Route.Login.path
    session!!.role == UserRole.TRAINER     -> Route.TrainerDashboard.path
    else                                   -> Route.ClientDashboard.path
}
```

## Guards

- El token y el rol se persisten en **DataStore** y se validan al arrancar (`SessionManager.restore()`).
- Si un 401 llega desde cualquier endpoint, el `AuthInterceptor` emite `GlobalEvent.SessionExpired` y el usuario es redirigido a Login borrando el back stack.

## ViewModels compartidos entre padre-hijo

Para pantallas padre-hijo dentro del mismo flujo, usar ViewModels con scope de `NavBackStackEntry` en lugar de ViewModels globales.
