package com.almanza.kochappi.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object Login : Route
    @Serializable data object ForgotPassword : Route
    @Serializable data object Home : Route

    // Trainer — Clients
    @Serializable data object ClientList : Route
    @Serializable data object ClientAdd : Route
    @Serializable data class ClientEdit(val clientId: Int) : Route

    // Trainer — Exercises
    @Serializable data object ExerciseList : Route
    @Serializable data object ExerciseAdd : Route
    @Serializable data class ExerciseEdit(val exerciseId: Int) : Route

    // Trainer — Templates
    @Serializable data object TemplateList : Route
    @Serializable data object TemplateAdd : Route
    @Serializable data class TemplateEdit(val templateId: Int) : Route
    @Serializable data class TemplateExerciseAdd(
        val templateId: Int,
        val dayIndex: Int,
        val nextDisplayOrder: Int,
    ) : Route

    @Serializable data class TemplateExerciseEdit(
        val templateId: Int,
        val dayIndex: Int,
        /** > 0 if editing a saved detail; -1 if editing a pending detail. */
        val detailId: Int,
        /** >= 0 (global index in pending list) if editing a pending detail; -1 if editing a saved detail. */
        val pendingIndex: Int,
        val exerciseId: Int,
        val sets: Int,
        val reps: Int,
        val displayOrder: Int,
    ) : Route

    // Trainer — Client Detail
    @Serializable data class ClientDetail(val clientId: Int) : Route

    // Trainer — Routines
    @Serializable data class RoutineAdd(
        val clientId: String = "",
        val templateId: String = "",
    ) : Route
    @Serializable data class RoutineEdit(val routineId: String) : Route
    @Serializable data class RoutineDetail(val routineId: String) : Route
    @Serializable data class RoutineExerciseAdd(
        val routineId: String,
        val dayIndex: Int,
    ) : Route
    @Serializable data class RoutineExerciseEdit(
        val routineId: String,
        val dayIndex: Int,
        val exerciseIndex: Int,
    ) : Route
    @Serializable data class RoutineSelectTemplate(val clientId: String) : Route

    // Client
    @Serializable data object ClientDashboard : Route

    // Settings
    @Serializable data object Settings : Route
}
