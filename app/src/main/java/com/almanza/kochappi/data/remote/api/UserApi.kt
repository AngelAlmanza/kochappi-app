package com.almanza.kochappi.data.remote.api

import com.almanza.kochappi.data.remote.dto.CreateUserRequest
import com.almanza.kochappi.data.remote.dto.RegisterResponseDto
import com.almanza.kochappi.data.remote.dto.UserDto
import com.almanza.kochappi.data.remote.dto.UserListDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    @GET("users")
    suspend fun getByRole(@Query("role") role: String, @Query("includeWithCustomers") includeWithCustomers: Boolean): List<UserListDto>

    @POST("users")
    suspend fun create(@Body body: CreateUserRequest): RegisterResponseDto
}
