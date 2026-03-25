package com.almanza.kochappi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- Requests --

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class ForgotPasswordRequest(
    val email: String,
)

@Serializable
data class ResetPasswordRequest(
    val email: String,
    @SerialName("otp_code") val otpCode: String,
    @SerialName("new_password") val newPassword: String,
)

@Serializable
data class RefreshTokenRequest(
    @SerialName("refresh_token") val refreshToken: String,
)

// -- Responses --

@Serializable
data class AuthResponseDto(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    val user: UserDto,
)

@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
)

@Serializable
data class TokenResponseDto(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
)

@Serializable
data class MessageResponseDto(
    val message: String,
)

@Serializable
data class ErrorResponseDto(
    val code: String,
    val error: String,
)
