package com.almanza.kochappi.data.remote.api

import com.almanza.kochappi.data.remote.dto.AuthResponseDto
import com.almanza.kochappi.data.remote.dto.ForgotPasswordRequest
import com.almanza.kochappi.data.remote.dto.LoginRequest
import com.almanza.kochappi.data.remote.dto.MessageResponseDto
import com.almanza.kochappi.data.remote.dto.RefreshTokenRequest
import com.almanza.kochappi.data.remote.dto.ResetPasswordRequest
import com.almanza.kochappi.data.remote.dto.TokenResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponseDto

    @POST("auth/refresh")
    suspend fun refreshToken(@Body body: RefreshTokenRequest): TokenResponseDto

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): MessageResponseDto

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): MessageResponseDto
}
