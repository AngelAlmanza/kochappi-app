package com.almanza.kochappi.domain.model

data class Session(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int,
    val role: UserRole,
    val displayName: String,
    val email: String,
)

enum class UserRole { TRAINER, CLIENT }
