package com.almanza.kochappi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserListDto(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
)

@Serializable
data class RegisterResponseDto(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
)

@Serializable
data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String
)
