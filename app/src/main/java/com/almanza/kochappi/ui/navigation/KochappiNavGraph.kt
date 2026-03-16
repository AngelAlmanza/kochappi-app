package com.almanza.kochappi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.almanza.kochappi.ui.auth.forgot.ForgotPasswordScreen
import com.almanza.kochappi.ui.auth.login.LoginScreen
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
fun KochappiNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.Login,
    ) {
        // ── Auth ──
        composable<Route.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Route.Home) {
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

        // ── Home ──
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

        // ── Trainer: Clients ──
        composable<Route.ClientList> {
            ClientListScreen(
                onBack = { navController.popBackStack() },
                onAddClient = {
                    navController.navigate(Route.ClientAdd)
                },
                onClientClick = { clientId ->
                    navController.navigate(Route.ClientDetail(clientId))
                },
            )
        }

        composable<Route.ClientAdd> {
            ClientFormScreen(
                clientId = null,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.ClientEdit> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.ClientEdit>()
            ClientFormScreen(
                clientId = route.clientId,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.ClientDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.ClientDetail>()
            ClientDetailScreen(
                clientId = route.clientId,
                onBack = { navController.popBackStack() },
                onEditClient = { clientId ->
                    navController.navigate(Route.ClientEdit(clientId))
                },
                onAssignRoutine = { clientId ->
                    navController.navigate(Route.RoutineAdd(clientId = clientId))
                },
                onRoutineClick = { routineId ->
                    navController.navigate(Route.RoutineDetail(routineId))
                },
            )
        }

        // ── Trainer: Exercises ──
        composable<Route.ExerciseList> {
            ExerciseListScreen(
                onBack = { navController.popBackStack() },
                onAddExercise = {
                    navController.navigate(Route.ExerciseAdd)
                },
                onExerciseClick = { exerciseId ->
                    navController.navigate(Route.ExerciseEdit(exerciseId))
                },
            )
        }

        composable<Route.ExerciseAdd> {
            ExerciseFormScreen(
                exerciseId = null,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.ExerciseEdit> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.ExerciseEdit>()
            ExerciseFormScreen(
                exerciseId = route.exerciseId,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }

        // ── Trainer: Templates ──
        composable<Route.TemplateList> {
            TemplateListScreen(
                onBack = { navController.popBackStack() },
                onAddTemplate = {
                    navController.navigate(Route.TemplateAdd)
                },
                onTemplateClick = { templateId ->
                    navController.navigate(Route.TemplateEdit(templateId))
                },
            )
        }

        composable<Route.TemplateAdd> {
            TemplateFormScreen(
                templateId = null,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                onAddExercise = { dayIndex ->
                    navController.navigate(Route.TemplateExerciseAdd("new", dayIndex))
                },
                onEditExercise = { dayIndex, exerciseIndex ->
                    navController.navigate(
                        Route.TemplateExerciseEdit("new", dayIndex, exerciseIndex)
                    )
                },
            )
        }

        composable<Route.TemplateEdit> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.TemplateEdit>()
            TemplateFormScreen(
                templateId = route.templateId,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                onAddExercise = { dayIndex ->
                    navController.navigate(
                        Route.TemplateExerciseAdd(route.templateId, dayIndex)
                    )
                },
                onEditExercise = { dayIndex, exerciseIndex ->
                    navController.navigate(
                        Route.TemplateExerciseEdit(route.templateId, dayIndex, exerciseIndex)
                    )
                },
            )
        }

        composable<Route.TemplateExerciseAdd> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.TemplateExerciseAdd>()
            TemplateExerciseFormScreen(
                dayIndex = route.dayIndex,
                exerciseIndex = null,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.TemplateExerciseEdit> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.TemplateExerciseEdit>()
            TemplateExerciseFormScreen(
                dayIndex = route.dayIndex,
                exerciseIndex = route.exerciseIndex,
                onSave = { navController.popBackStack() },
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
