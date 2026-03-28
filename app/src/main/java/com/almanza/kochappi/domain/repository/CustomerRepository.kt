package com.almanza.kochappi.domain.repository

import com.almanza.kochappi.domain.model.Customer

interface CustomerRepository {
    suspend fun getAll(): List<Customer>
    suspend fun getById(id: Int): Customer
    suspend fun create(userId: Int, name: String, birthdate: String): Customer
    suspend fun update(id: Int, name: String, birthdate: String): Customer
    suspend fun delete(id: Int)
}
