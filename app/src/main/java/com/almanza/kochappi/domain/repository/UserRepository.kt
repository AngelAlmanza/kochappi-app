package com.almanza.kochappi.domain.repository

import com.almanza.kochappi.domain.model.User

interface UserRepository {
    suspend fun getByRole(role: String, includeWithCustomers: Boolean): List<User>
    suspend fun create(name: String, email: String, password: String, role: String): User
}
