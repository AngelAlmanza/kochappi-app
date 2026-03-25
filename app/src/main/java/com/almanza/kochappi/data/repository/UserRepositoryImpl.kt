package com.almanza.kochappi.data.repository

import com.almanza.kochappi.data.mapper.toDomain
import com.almanza.kochappi.data.remote.api.UserApi
import com.almanza.kochappi.data.remote.dto.RegisterRequest
import com.almanza.kochappi.domain.model.User
import com.almanza.kochappi.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
) : UserRepository {

    override suspend fun getByRole(role: String): List<User> =
        userApi.getByRole(role).map { it.toDomain() }

    override suspend fun register(name: String, email: String, password: String, role: String): User =
        userApi.register(RegisterRequest(name, email, password, role)).toDomain()
}
