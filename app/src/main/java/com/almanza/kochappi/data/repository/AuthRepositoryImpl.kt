package com.almanza.kochappi.data.repository

import com.almanza.kochappi.data.mapper.toDomain
import com.almanza.kochappi.data.remote.api.AuthApi
import com.almanza.kochappi.data.remote.dto.ForgotPasswordRequest
import com.almanza.kochappi.data.remote.dto.LoginRequest
import com.almanza.kochappi.data.remote.dto.ResetPasswordRequest
import com.almanza.kochappi.data.session.SessionManager
import com.almanza.kochappi.domain.model.MessageResponse
import com.almanza.kochappi.domain.model.Session
import com.almanza.kochappi.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager,
) : AuthRepository {

    override suspend fun login(email: String, password: String): Session {
        val response = authApi.login(LoginRequest(email, password))
        val session = response.toDomain()
        sessionManager.login(session)
        return session
    }

    override suspend fun forgotPassword(email: String): MessageResponse {
        return authApi.forgotPassword(ForgotPasswordRequest(email)).toDomain()
    }

    override suspend fun resetPassword(
        email: String,
        otpCode: String,
        newPassword: String,
    ): MessageResponse {
        return authApi.resetPassword(
            ResetPasswordRequest(email, otpCode, newPassword)
        ).toDomain()
    }
}
