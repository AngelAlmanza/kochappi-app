package com.almanza.kochappi.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: UserRole
)
