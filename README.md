# Kochappi

> **Aplicación Android para gestión de rutinas de entrenamiento personal y seguimiento de progreso**

**Estado:** En desarrollo
**Versión:** 1.2
**Fecha:** Marzo 2026

## Descripción

Kochappi es una app Android nativa que facilita la gestión integral entre entrenador personal y sus clientes. Centraliza la asignación de rutinas, registro de sesiones, seguimiento del progreso físico y comunicación del desempeño.

### Operación por roles

- **Entrenador**: Diseña rutinas, supervisa progreso, registra sesiones presenciales
- **Cliente**: Ejecuta rutinas, registra entrenamiento, visualiza su evolución

## Tech Stack

| Categoría | Tecnología |
|---|---|
| **Plataforma** | Android API 26+ |
| **UI Framework** | Jetpack Compose |
| **Arquitectura** | MVVM + Repository Pattern |
| **DI** | Hilt |
| **HTTP** | Retrofit + OkHttp |
| **DB Local** | Room |
| **Serialización** | Kotlinx Serialization |
| **Async** | Coroutines + Flow |

## Documentación

- **[Product Requirements Document](docs/PRD/)** — Funcionalidades, requerimientos, flujos
- **[Arquitectura](docs/architecture/)** — Patrones, estructura de paquetes, estado global

## Estructura del Proyecto

```
app/src/main/java/com/almanza/kochappi/
├── ui/              # Composables, ViewModels
├── data/            # Repositorios, datasources, mappers
├── domain/          # Modelos de dominio, interfaces
└── common/          # Utilidades, constantes
```

## Requisitos

- Android SDK 26+
- Android Studio 2024.1+
- JDK 11+

## Desarrollo

### Convenciones de commits

Seguir el formato: `type(scope): description`

Tipos: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

Ejemplo:
```
feat(auth): add login screen with email validation
fix(routines): correct weight calculation for percentage-based loads
```
