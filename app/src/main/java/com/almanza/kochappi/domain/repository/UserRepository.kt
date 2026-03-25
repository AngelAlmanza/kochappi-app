package com.almanza.kochappi.domain.repository

import com.almanza.kochappi.domain.model.User

interface UserRepository {
    suspend fun getByRole(role: String): List<User>
    suspend fun register(name: String, email: String, password: String, role: String): User
}
