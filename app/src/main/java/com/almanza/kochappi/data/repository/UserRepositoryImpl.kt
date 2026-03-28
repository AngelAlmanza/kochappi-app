package com.almanza.kochappi.data.repository

import com.almanza.kochappi.data.mapper.toDomain
import com.almanza.kochappi.data.remote.api.UserApi
import com.almanza.kochappi.data.remote.dto.CreateUserRequest
import com.almanza.kochappi.domain.model.User
import com.almanza.kochappi.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
) : UserRepository {

    override suspend fun getByRole(role: String, includeWithCustomers: Boolean): List<User> =
        userApi.getByRole(role, includeWithCustomers).map { it.toDomain() }

    override suspend fun create(name: String, email: String, password: String, role: String): User =
        userApi.create(CreateUserRequest(name, email, password, role)).toDomain()
}
