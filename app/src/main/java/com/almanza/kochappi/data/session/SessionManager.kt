package com.almanza.kochappi.data.session

import com.almanza.kochappi.data.local.datastore.SessionPreferences
import com.almanza.kochappi.domain.model.Session
import com.almanza.kochappi.domain.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val sessionPreferences: SessionPreferences,
) {
    private val _session = MutableStateFlow<Session?>(null)
    val session: StateFlow<Session?> = _session.asStateFlow()

    val currentToken: String? get() = _session.value?.accessToken
    val currentRefreshToken: String? get() = _session.value?.refreshToken

    suspend fun login(session: Session) {
        _session.value = session
        sessionPreferences.save(session.toStored())
    }

    suspend fun updateTokens(accessToken: String, refreshToken: String) {
        val current = _session.value ?: return
        val updated = current.copy(accessToken = accessToken, refreshToken = refreshToken)
        _session.value = updated
        sessionPreferences.save(updated.toStored())
    }

    suspend fun logout() {
        _session.value = null
        sessionPreferences.clear()
    }

    suspend fun restore(): Session? {
        val stored = sessionPreferences.load() ?: return null
        val session = Session(
            accessToken = stored.accessToken,
            refreshToken = stored.refreshToken,
            userId = stored.userId.toIntOrNull() ?: return null,
            role = try { UserRole.valueOf(stored.role) } catch (_: Exception) { return null },
            displayName = stored.displayName,
            email = stored.email,
        )
        _session.value = session
        return session
    }

    private fun Session.toStored() = SessionPreferences.StoredSession(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId.toString(),
        role = role.name,
        displayName = displayName,
        email = email,
    )
}
