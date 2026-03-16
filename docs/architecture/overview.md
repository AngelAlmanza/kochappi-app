# Overview — Android App

## Arquitectura
**MVVM + Repository Pattern** | Android API 26+ | Jetpack Compose

```
UI (Composable) → ViewModel → Repository (interfaz) → Impl → Remote / Local
                                                            ↓
                                                         Mapper → Domain Model
```

## Stack de Librerías

| Categoría | Librería |
|---|---|
| DI | Hilt |
| HTTP | Retrofit + OkHttp |
| Serialización | Kotlinx Serialization |
| DB Local | Room |
| Preferences | DataStore |
| Navegación | Navigation Compose (type-safe) |
| Async | Coroutines + Flow |
| Imágenes | Coil |

## Reglas Base

- **UI** solo conoce ViewModels.
- **ViewModels** solo conocen interfaces de Repository (paquete `domain`).
- **Implementations** en `data/` manejan red, DB y caché.
- **Mappers** convierten DTOs/Entities → Domain models. Nunca exponer DTOs a la UI.
- **Un ViewModel por pantalla.** No compartir entre pantallas salvo estado global.
