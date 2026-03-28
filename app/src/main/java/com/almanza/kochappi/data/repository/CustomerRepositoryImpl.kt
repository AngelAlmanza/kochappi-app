package com.almanza.kochappi.data.repository

import com.almanza.kochappi.data.mapper.toDomain
import com.almanza.kochappi.data.remote.api.CustomerApi
import com.almanza.kochappi.data.remote.dto.CreateCustomerRequest
import com.almanza.kochappi.data.remote.dto.UpdateCustomerRequest
import com.almanza.kochappi.domain.model.Customer
import com.almanza.kochappi.domain.repository.CustomerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepositoryImpl @Inject constructor(
    private val customerApi: CustomerApi,
) : CustomerRepository {

    override suspend fun getAll(): List<Customer> =
        customerApi.getAll().map { it.toDomain() }

    override suspend fun getById(id: Int): Customer =
        customerApi.getById(id).toDomain()

    override suspend fun create(userId: Int, name: String, birthdate: String): Customer =
        customerApi.create(CreateCustomerRequest(userId, name, birthdate)).toDomain()

    override suspend fun update(id: Int, name: String, birthdate: String): Customer =
        customerApi.update(id, UpdateCustomerRequest(name, birthdate)).toDomain()

    override suspend fun delete(id: Int) =
        customerApi.delete(id)
}
