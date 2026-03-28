package com.almanza.kochappi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.almanza.kochappi.domain.model.CreateDetailParams
import com.almanza.kochappi.domain.model.UserRole
import com.almanza.kochappi.ui.trainer.clients.ClientDetailViewModel
import com.almanza.kochappi.ui.trainer.clients.ClientListViewModel
import com.almanza.kochappi.ui.trainer.exercises.ExerciseListViewModel
import com.almanza.kochappi.ui.trainer.templates.TemplateFormViewModel
import com.almanza.kochappi.ui.trainer.templates.TemplateListViewModel
import com.almanza.kochappi.ui.auth.forgot.ForgotPasswordScreen
import com.almanza.kochappi.ui.auth.login.LoginScreen
import com.almanza.kochappi.ui.client.dashboard.ClientDashboardScreen
import com.almanza.kochappi.ui.home.HomeScreen
import com.almanza.kochappi.ui.trainer.clients.ClientDetailScreen
import com.almanza.kochappi.ui.trainer.clients.ClientFormScreen
import com.almanza.kochappi.ui.trainer.clients.ClientListScreen
import com.almanza.kochappi.ui.trainer.exercises.ExerciseFormScreen
import com.almanza.kochappi.ui.trainer.exercises.ExerciseListScreen
import com.almanza.kochappi.ui.settings.SettingsScreen
import com.almanza.kochappi.ui.trainer.routines.RoutineDetailScreen
import com.almanza.kochappi.ui.trainer.routines.RoutineExerciseFormScreen
import com.almanza.kochappi.ui.trainer.routines.RoutineFormScreen
import com.almanza.kochappi.ui.trainer.routines.RoutineSelectTemplateScreen
import com.almanza.kochappi.ui.trainer.templates.TemplateExerciseFormScreen
import com.almanza.kochappi.ui.trainer.templates.TemplateFormScreen
import com.almanza.kochappi.ui.trainer.templates.TemplateListScreen

@Composable
fun KochappiNavGraph(
    navController: NavHostController,
    startDestination: Route = Route.Login,
    displayName: String = "",
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        // ── Auth ──
        composable<Route.Login> {
            LoginScreen(
                onLoginSuccess = { role ->
                    val destination = when (role) {
                        UserRole.TRAINER -> Route.Home
                        UserRole.CLIENT -> Route.ClientDashboard
                    }
                    navController.navigate(destination) {
                        popUpTo(Route.Login) { inclusive = true }
                    }
                },
                onForgotPassword = {
                    navController.navigate(Route.ForgotPassword)
                },
            )
        }

        composable<Route.ForgotPassword> {
            ForgotPasswordScreen(
                onPasswordReset = {
                    navController.navigate(Route.Login) {
                        popUpTo(Route.Login) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }

        // ── Home (Trainer) ──
        composable<Route.Home> {
            HomeScreen(
                onNavigateToClients = {
                    navController.navigate(Route.ClientList)
                },
                onNavigateToExercises = {
                    navController.navigate(Route.ExerciseList)
                },
                onNavigateToTemplates = {
                    navController.navigate(Route.TemplateList)
                },
                onNavigateToSettings = {
                    navController.navigate(Route.Settings)
                },
            )
        }

        // ── Client Dashboard ──
        composable<Route.ClientDashboard> {
            ClientDashboardScreen(
                displayName = displayName,
                onNavigateToSettings = {
                    navController.navigate(Route.Settings)
                },
            )
        }

        // ── Trainer: Clients ──
        composable<Route.ClientList> { backStackEntry ->
            val viewModel: ClientListViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                backStackEntry.savedStateHandle
                    .getStateFlow("refresh_clients", false)
                    .collect { shouldRefresh ->
                        if (shouldRefresh) {
                            viewModel.loadClients()
                            backStackEntry.savedStateHandle.remove<Boolean>("refresh_clients")
                        }
                    }
            }

            ClientListScreen(
                onBack = { navController.popBackStack() },
                onAddClient = { navController.navigate(Route.ClientAdd) },
                onClientClick = { clientId -> navController.navigate(Route.ClientDetail(clientId)) },
                onEditClient = { clientId -> navController.navigate(Route.ClientEdit(clientId)) },
                onDeleteClient = { clientId -> viewModel.deleteClient(clientId) },
                viewModel = viewModel,
            )
        }

        composable<Route.ClientAdd> {
            ClientFormScreen(
                clientId = null,
                onSave = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh_clients", true)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.ClientEdit> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.ClientEdit>()
            ClientFormScreen(
                clientId = route.clientId,
                onSave = {
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set("refresh_client", true)   // para ClientDetail
                        set("refresh_clients", true)  // para ClientList
                    }
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.ClientDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.ClientDetail>()
            val viewModel: ClientDetailViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                backStackEntry.savedStateHandle
                    .getStateFlow("refresh_client", false)
                    .collect { shouldRefresh ->
                        if (shouldRefresh) {
                            viewModel.loadClient(route.clientId)
                            backStackEntry.savedStateHandle.remove<Boolean>("refresh_client")
                        }
                    }
            }

            ClientDetailScreen(
                clientId = route.clientId,
                onBack = { navController.popBackStack() },
                onEditClient = { clientId -> navController.navigate(Route.ClientEdit(clientId)) },
                onClientDeleted = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh_clients", true)
                    navController.popBackStack()
                },
                onAssignRoutine = { clientId -> navController.navigate(Route.RoutineAdd(clientId = clientId)) },
                onRoutineClick = { routineId -> navController.navigate(Route.RoutineDetail(routineId)) },
                viewModel = viewModel,
            )
        }

        // ── Trainer: Exercises ──
        composable<Route.ExerciseList> { backStackEntry ->
            val viewModel: ExerciseListViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                backStackEntry.savedStateHandle
                    .getStateFlow("refresh_exercises", false)
                    .collect { shouldRefresh ->
                        if (shouldRefresh) {
                            viewModel.loadExercises()
                            backStackEntry.savedStateHandle.remove<Boolean>("refresh_exercises")
                        }
                    }
            }

            ExerciseListScreen(
                onBack = { navController.popBackStack() },
                onAddExercise = {
                    navController.navigate(Route.ExerciseAdd)
                },
                onExerciseClick = { exerciseId ->
                    navController.navigate(Route.ExerciseEdit(exerciseId))
                },
                viewModel = viewModel,
            )
        }

        composable<Route.ExerciseAdd> {
            ExerciseFormScreen(
                exerciseId = null,
                onSave = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh_exercises", true)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.ExerciseEdit> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.ExerciseEdit>()
            ExerciseFormScreen(
                exerciseId = route.exerciseId,
                onSave = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh_exercises", true)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() },
            )
        }

        // ── Trainer: Templates ──
        composable<Route.TemplateList> { backStackEntry ->
            val viewModel: TemplateListViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                backStackEntry.savedStateHandle
                    .getStateFlow("refresh_templates", false)
                    .collect { shouldRefresh ->
                        if (shouldRefresh) {
                            viewModel.loadTemplates()
                            backStackEntry.savedStateHandle.remove<Boolean>("refresh_templates")
                        }
                    }
            }

            TemplateListScreen(
                onBack = { navController.popBackStack() },
                onAddTemplate = {
                    navController.navigate(Route.TemplateAdd)
                },
                onTemplateClick = { templateId ->
                    navController.navigate(Route.TemplateEdit(templateId))
                },
                viewModel = viewModel,
            )
        }

        composable<Route.TemplateAdd> { backStackEntry ->
            val viewModel: TemplateFormViewModel = hiltViewModel()

            // Observe results from TemplateExerciseAdd / TemplateExerciseEdit
            LaunchedEffect(Unit) {
                val handle = backStackEntry.savedStateHandle
                handle.getStateFlow("result_exerciseId", -1).collect { exerciseId ->
                    if (exerciseId > 0) {
                        val dayOfWeek = handle.get<Int>("result_dayOfWeek") ?: return@collect
                        val displayOrder = handle.get<Int>("result_displayOrder") ?: return@collect
                        val sets = handle.get<Int>("result_sets") ?: return@collect
                        val reps = handle.get<Int>("result_reps") ?: return@collect
                        val detailId = handle.get<Int>("result_detailId") ?: -1
                        val pendingIndex = handle.get<Int>("result_pendingIndex") ?: -1
                        val params = CreateDetailParams(exerciseId, dayOfWeek, displayOrder, sets, reps)
                        when {
                            detailId > 0 -> viewModel.replaceSavedDetail(detailId, params)
                            pendingIndex >= 0 -> viewModel.updatePendingDetail(pendingIndex, params)
                            else -> viewModel.addPendingDetail(params)
                        }
                        handle.remove<Int>("result_exerciseId")
                    }
                }
            }

            TemplateFormScreen(
                templateId = null,
                onSave = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh_templates", true)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() },
                onAddExercise = { dayIndex, nextDisplayOrder ->
                    navController.navigate(Route.TemplateExerciseAdd(0, dayIndex, nextDisplayOrder))
                },
                onEditExercise = { detailId, pendingIndex, dayIndex, exerciseId, sets, reps, displayOrder ->
                    navController.navigate(
                        Route.TemplateExerciseEdit(0, dayIndex, detailId, pendingIndex, exerciseId, sets, reps, displayOrder)
                    )
                },
                viewModel = viewModel,
            )
        }

        composable<Route.TemplateEdit> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.TemplateEdit>()
            val viewModel: TemplateFormViewModel = hiltViewModel()

            // Observe results from TemplateExerciseAdd / TemplateExerciseEdit
            LaunchedEffect(Unit) {
                val handle = backStackEntry.savedStateHandle
                handle.getStateFlow("result_exerciseId", -1).collect { exerciseId ->
                    if (exerciseId > 0) {
                        val dayOfWeek = handle.get<Int>("result_dayOfWeek") ?: return@collect
                        val displayOrder = handle.get<Int>("result_displayOrder") ?: return@collect
                        val sets = handle.get<Int>("result_sets") ?: return@collect
                        val reps = handle.get<Int>("result_reps") ?: return@collect
                        val detailId = handle.get<Int>("result_detailId") ?: -1
                        val pendingIndex = handle.get<Int>("result_pendingIndex") ?: -1
                        val params = CreateDetailParams(exerciseId, dayOfWeek, displayOrder, sets, reps)
                        when {
                            detailId > 0 -> viewModel.replaceSavedDetail(detailId, params)
                            pendingIndex >= 0 -> viewModel.updatePendingDetail(pendingIndex, params)
                            else -> viewModel.addPendingDetail(params)
                        }
                        handle.remove<Int>("result_exerciseId")
                    }
                }
            }

            TemplateFormScreen(
                templateId = route.templateId,
                onSave = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh_templates", true)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() },
                onAddExercise = { dayIndex, nextDisplayOrder ->
                    navController.navigate(
                        Route.TemplateExerciseAdd(route.templateId, dayIndex, nextDisplayOrder)
                    )
                },
                onEditExercise = { detailId, pendingIndex, dayIndex, exerciseId, sets, reps, displayOrder ->
                    navController.navigate(
                        Route.TemplateExerciseEdit(route.templateId, dayIndex, detailId, pendingIndex, exerciseId, sets, reps, displayOrder)
                    )
                },
                viewModel = viewModel,
            )
        }

        composable<Route.TemplateExerciseAdd> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.TemplateExerciseAdd>()
            TemplateExerciseFormScreen(
                dayIndex = route.dayIndex,
                initialDisplayOrder = route.nextDisplayOrder,
                onSave = { exerciseId, dayOfWeek, displayOrder, sets, reps ->
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set("result_exerciseId", exerciseId)
                        set("result_dayOfWeek", dayOfWeek)
                        set("result_displayOrder", displayOrder)
                        set("result_sets", sets)
                        set("result_reps", reps)
                        // No result_detailId / result_pendingIndex → treated as a new addition
                    }
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.TemplateExerciseEdit> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.TemplateExerciseEdit>()
            TemplateExerciseFormScreen(
                dayIndex = route.dayIndex,
                initialDisplayOrder = route.displayOrder,
                initialExerciseId = route.exerciseId,
                initialSets = route.sets,
                initialReps = route.reps,
                onSave = { exerciseId, dayOfWeek, displayOrder, sets, reps ->
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set("result_exerciseId", exerciseId)
                        set("result_dayOfWeek", dayOfWeek)
                        set("result_displayOrder", displayOrder)
                        set("result_sets", sets)
                        set("result_reps", reps)
                        set("result_detailId", route.detailId)
                        set("result_pendingIndex", route.pendingIndex)
                    }
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() },
            )
        }

        // ── Trainer: Routines ──
        composable<Route.RoutineAdd> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.RoutineAdd>()
            RoutineFormScreen(
                routineId = null,
                clientId = route.clientId,
                templateId = route.templateId,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                onSelectTemplate = { clientId ->
                    navController.navigate(Route.RoutineSelectTemplate(clientId))
                },
                onAddExercise = { dayIndex ->
                    navController.navigate(Route.RoutineExerciseAdd("new", dayIndex))
                },
                onEditExercise = { dayIndex, exerciseIndex ->
                    navController.navigate(
                        Route.RoutineExerciseEdit("new", dayIndex, exerciseIndex)
                    )
                },
            )
        }

        composable<Route.RoutineEdit> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.RoutineEdit>()
            RoutineFormScreen(
                routineId = route.routineId,
                clientId = "",
                templateId = "",
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                onSelectTemplate = {},
                onAddExercise = { dayIndex ->
                    navController.navigate(
                        Route.RoutineExerciseAdd(route.routineId, dayIndex)
                    )
                },
                onEditExercise = { dayIndex, exerciseIndex ->
                    navController.navigate(
                        Route.RoutineExerciseEdit(route.routineId, dayIndex, exerciseIndex)
                    )
                },
            )
        }

        composable<Route.RoutineDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.RoutineDetail>()
            RoutineDetailScreen(
                routineId = route.routineId,
                onBack = { navController.popBackStack() },
                onEdit = { routineId ->
                    navController.navigate(Route.RoutineEdit(routineId))
                },
            )
        }

        composable<Route.RoutineExerciseAdd> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.RoutineExerciseAdd>()
            RoutineExerciseFormScreen(
                dayIndex = route.dayIndex,
                exerciseIndex = null,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.RoutineExerciseEdit> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.RoutineExerciseEdit>()
            RoutineExerciseFormScreen(
                dayIndex = route.dayIndex,
                exerciseIndex = route.exerciseIndex,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.RoutineSelectTemplate> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.RoutineSelectTemplate>()
            RoutineSelectTemplateScreen(
                clientId = route.clientId,
                onBack = { navController.popBackStack() },
                onTemplateSelected = { templateId ->
                    navController.navigate(
                        Route.RoutineAdd(
                            clientId = route.clientId,
                            templateId = templateId,
                        )
                    ) {
                        popUpTo<Route.RoutineAdd> { inclusive = true }
                    }
                },
            )
        }

        // ── Settings ──
        composable<Route.Settings> {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Route.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}
