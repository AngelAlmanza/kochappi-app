package com.almanza.kochappi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CustomerDto(
    val id: Int,
    val name: String,
    val birthdate: String,
)

@Serializable
data class CreateCustomerRequest(
    val userId: Int,
    val name: String,
    val birthdate: String,
)

@Serializable
data class UpdateCustomerRequest(
    val name: String,
    val birthdate: String,
)
