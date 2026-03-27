package com.almanza.kochappi.data.repository

import com.almanza.kochappi.data.mapper.toDomain
import com.almanza.kochappi.data.remote.api.ExerciseApi
import com.almanza.kochappi.data.remote.dto.ExerciseRequest
import com.almanza.kochappi.domain.model.Exercise
import com.almanza.kochappi.domain.repository.ExerciseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseApi: ExerciseApi,
) : ExerciseRepository {

    override suspend fun getExercises(): List<Exercise> =
        exerciseApi.getExercises().map { it.toDomain() }

    override suspend fun getExercise(id: Int): Exercise =
        exerciseApi.getExercise(id).toDomain()

    override suspend fun createExercise(name: String, videoUrl: String?): Exercise =
        exerciseApi.createExercise(ExerciseRequest(name, videoUrl)).toDomain()

    override suspend fun updateExercise(id: Int, name: String, videoUrl: String?): Exercise =
        exerciseApi.updateExercise(id, ExerciseRequest(name, videoUrl)).toDomain()

    override suspend fun deleteExercise(id: Int) =
        exerciseApi.deleteExercise(id)
}
