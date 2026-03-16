package com.almanza.kochappi.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
}

@Singleton
class ThemePreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private companion object {
        val THEME_KEY = stringPreferencesKey("theme_mode")
    }

    val themeMode: Flow<ThemeMode> = dataStore.data.map { prefs ->
        val name = prefs[THEME_KEY] ?: ThemeMode.SYSTEM.name
        try {
            ThemeMode.valueOf(name)
        } catch (_: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { prefs ->
            prefs[THEME_KEY] = mode.name
        }
    }
}
