package com.almanza.kochappi.data.remote.interceptor

import com.almanza.kochappi.data.remote.api.AuthApi
import com.almanza.kochappi.data.remote.dto.RefreshTokenRequest
import com.almanza.kochappi.data.session.GlobalEvent
import com.almanza.kochappi.data.session.GlobalEventBus
import com.almanza.kochappi.data.session.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val sessionManager: SessionManager,
    private val globalEventBus: GlobalEventBus,
    private val authApiProvider: Provider<AuthApi>,
) : Authenticator {

    private val lock = Any()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.url.encodedPath.contains("auth/refresh")) {
            return null
        }

        synchronized(lock) {
            val tokenUsedInRequest = response.request.header("Authorization")
                ?.removePrefix("Bearer ")

            val currentToken = sessionManager.currentToken
            if (currentToken != null && currentToken != tokenUsedInRequest) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            val refreshToken = sessionManager.currentRefreshToken ?: run {
                globalEventBus.emit(GlobalEvent.SessionExpired)
                return null
            }

            return try {
                val tokenResponse = runBlocking {
                    authApiProvider.get().refreshToken(RefreshTokenRequest(refreshToken))
                }
                runBlocking {
                    sessionManager.updateTokens(
                        accessToken = tokenResponse.accessToken,
                        refreshToken = tokenResponse.refreshToken,
                    )
                }
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${tokenResponse.accessToken}")
                    .build()
            } catch (_: Exception) {
                runBlocking { sessionManager.logout() }
                globalEventBus.emit(GlobalEvent.SessionExpired)
                null
            }
        }
    }
}
