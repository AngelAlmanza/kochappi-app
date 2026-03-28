package com.almanza.kochappi.data.remote.api

import com.almanza.kochappi.data.remote.dto.CreateCustomerRequest
import com.almanza.kochappi.data.remote.dto.CustomerDto
import com.almanza.kochappi.data.remote.dto.UpdateCustomerRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomerApi {

    @GET("customers")
    suspend fun getAll(): List<CustomerDto>

    @GET("customers/{id}")
    suspend fun getById(@Path("id") id: Int): CustomerDto

    @POST("customers")
    suspend fun create(@Body body: CreateCustomerRequest): CustomerDto

    @PUT("customers/{id}")
    suspend fun update(@Path("id") id: Int, @Body body: UpdateCustomerRequest): CustomerDto

    @DELETE("customers/{id}")
    suspend fun delete(@Path("id") id: Int)
}
