package com.almanza.kochappi.data.remote.api

import com.almanza.kochappi.data.remote.dto.ExerciseDto
import com.almanza.kochappi.data.remote.dto.ExerciseRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ExerciseApi {

    @GET("exercises")
    suspend fun getExercises(): List<ExerciseDto>

    @POST("exercises")
    suspend fun createExercise(@Body body: ExerciseRequest): ExerciseDto

    @GET("exercises/{id}")
    suspend fun getExercise(@Path("id") id: Int): ExerciseDto

    @PUT("exercises/{id}")
    suspend fun updateExercise(@Path("id") id: Int, @Body body: ExerciseRequest): ExerciseDto

    @DELETE("exercises/{id}")
    suspend fun deleteExercise(@Path("id") id: Int)
}
