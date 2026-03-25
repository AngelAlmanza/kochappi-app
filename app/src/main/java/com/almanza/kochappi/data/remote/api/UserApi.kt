package com.almanza.kochappi.data.remote.api

import com.almanza.kochappi.data.remote.dto.RegisterRequest
import com.almanza.kochappi.data.remote.dto.RegisterResponseDto
import com.almanza.kochappi.data.remote.dto.UserListDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    @GET("users")
    suspend fun getByRole(@Query("role") role: String): List<UserListDto>

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): RegisterResponseDto
}
