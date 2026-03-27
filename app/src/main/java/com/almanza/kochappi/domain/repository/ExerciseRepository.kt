package com.almanza.kochappi.domain.repository

import com.almanza.kochappi.domain.model.Exercise

interface ExerciseRepository {
    suspend fun getExercises(): List<Exercise>
    suspend fun getExercise(id: Int): Exercise
    suspend fun createExercise(name: String, videoUrl: String?): Exercise
    suspend fun updateExercise(id: Int, name: String, videoUrl: String?): Exercise
    suspend fun deleteExercise(id: Int)
}
