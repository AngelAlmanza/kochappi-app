package com.almanza.kochappi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDto(
    val id: Int,
    val name: String,
    val videoUrl: String? = null,
)

@Serializable
data class ExerciseRequest(
    val name: String,
    val videoUrl: String? = null,
)
