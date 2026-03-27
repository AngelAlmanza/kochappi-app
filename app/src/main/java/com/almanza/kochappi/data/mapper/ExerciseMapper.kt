package com.almanza.kochappi.data.mapper

import com.almanza.kochappi.data.remote.dto.ExerciseDto
import com.almanza.kochappi.domain.model.Exercise

fun ExerciseDto.toDomain(): Exercise = Exercise(
    id = id,
    name = name,
    videoUrl = videoUrl,
)
