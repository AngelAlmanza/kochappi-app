package com.almanza.kochappi.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val ROLE_KEY = stringPreferencesKey("user_role")
        val DISPLAY_NAME_KEY = stringPreferencesKey("display_name")
        val EMAIL_KEY = stringPreferencesKey("email")
    }

    data class StoredSession(
        val accessToken: String,
        val refreshToken: String,
        val userId: String,
        val role: String,
        val displayName: String,
        val email: String,
    )

    suspend fun save(stored: StoredSession) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = stored.accessToken
            prefs[REFRESH_TOKEN_KEY] = stored.refreshToken
            prefs[USER_ID_KEY] = stored.userId
            prefs[ROLE_KEY] = stored.role
            prefs[DISPLAY_NAME_KEY] = stored.displayName
            prefs[EMAIL_KEY] = stored.email
        }
    }

    suspend fun load(): StoredSession? {
        val prefs = dataStore.data.first()
        val token = prefs[ACCESS_TOKEN_KEY] ?: return null
        return StoredSession(
            accessToken = token,
            refreshToken = prefs[REFRESH_TOKEN_KEY] ?: return null,
            userId = prefs[USER_ID_KEY] ?: return null,
            role = prefs[ROLE_KEY] ?: return null,
            displayName = prefs[DISPLAY_NAME_KEY] ?: "",
            email = prefs[EMAIL_KEY] ?: "",
        )
    }

    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
            prefs.remove(USER_ID_KEY)
            prefs.remove(ROLE_KEY)
            prefs.remove(DISPLAY_NAME_KEY)
            prefs.remove(EMAIL_KEY)
        }
    }
}
