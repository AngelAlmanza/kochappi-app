# Estructura de Paquetes

```
com.tuapp/
├── di/                         # Módulos de Hilt
├── data/
│   ├── remote/
│   │   ├── api/                # Interfaces Retrofit (ApiService)
│   │   ├── dto/                # Data Transfer Objects (request/response)
│   │   └── interceptor/        # Auth interceptor, token refresh
│   ├── local/
│   │   ├── dao/                # DAOs de Room
│   │   ├── entity/             # Entidades Room
│   │   └── datastore/          # Preferences DataStore
│   ├── mapper/                 # DTO <-> Domain, Entity <-> Domain
│   ├── repository/             # Implementaciones de Repository
│   └── session/                # SessionManager
├── domain/
│   ├── model/                  # Modelos de dominio (sin anotaciones de framework)
│   └── repository/             # Interfaces de Repository (contratos)
├── ui/
│   ├── navigation/             # NavGraph, rutas, guards por rol
│   ├── common/                 # Componentes reutilizables, UiState
│   ├── auth/                   # Login, Register (screen + viewmodel)
│   ├── trainer/                # Pantallas del entrenador
│   │   ├── dashboard/
│   │   ├── clients/
│   │   └── routines/
│   └── client/                 # Pantallas del cliente
│       ├── dashboard/
│       ├── progress/
│       └── routines/
└── util/                       # Extensions, constants, helpers
```

## Notas

- Los modelos en `domain/model/` son clases Kotlin puras, sin anotaciones de Room ni Retrofit.
- La interfaz del Repository vive en `domain/repository/`, la implementación en `data/repository/`.
  Esto permite cambiar Retrofit u agregar caché sin tocar ViewModels.
