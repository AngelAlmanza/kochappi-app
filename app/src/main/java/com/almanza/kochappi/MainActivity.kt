package com.almanza.kochappi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.almanza.kochappi.data.local.datastore.ThemeMode
import com.almanza.kochappi.data.local.datastore.ThemePreferences
import com.almanza.kochappi.data.session.GlobalEvent
import com.almanza.kochappi.data.session.GlobalEventBus
import com.almanza.kochappi.data.session.SessionManager
import com.almanza.kochappi.domain.model.UserRole
import com.almanza.kochappi.ui.navigation.KochappiNavGraph
import com.almanza.kochappi.ui.navigation.Route
import com.almanza.kochappi.ui.theme.KochappiTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themePreferences: ThemePreferences

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var globalEventBus: GlobalEventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by themePreferences.themeMode
                .collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)

            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            var isReady by remember { mutableStateOf(false) }
            var startDestination by remember { mutableStateOf<Route>(Route.Login) }
            var displayName by remember { mutableStateOf("") }

            LaunchedEffect(Unit) {
                val session = sessionManager.restore()
                if (session != null) {
                    displayName = session.displayName
                    startDestination = when (session.role) {
                        UserRole.TRAINER -> Route.Home
                        UserRole.CLIENT -> Route.ClientDashboard
                    }
                }
                isReady = true
            }

            KochappiTheme(darkTheme = darkTheme) {
                if (!isReady) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    val navController = rememberNavController()

                    LaunchedEffect(Unit) {
                        globalEventBus.events.collect { event ->
                            when (event) {
                                is GlobalEvent.SessionExpired -> {
                                    navController.navigate(Route.Login) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                                is GlobalEvent.ShowSnackbar -> {
                                    // Handled by individual screens
                                }
                            }
                        }
                    }

                    KochappiNavGraph(
                        navController = navController,
                        startDestination = startDestination,
                        displayName = displayName,
                    )
                }
            }
        }
    }
}
