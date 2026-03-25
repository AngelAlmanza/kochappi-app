package com.almanza.kochappi.domain.repository

import com.almanza.kochappi.domain.model.MessageResponse
import com.almanza.kochappi.domain.model.Session

interface AuthRepository {
    suspend fun login(email: String, password: String): Session
    suspend fun forgotPassword(email: String): MessageResponse
    suspend fun resetPassword(email: String, otpCode: String, newPassword: String): MessageResponse
}
