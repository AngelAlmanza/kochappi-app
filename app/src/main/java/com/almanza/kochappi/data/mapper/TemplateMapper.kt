package com.almanza.kochappi.data.mapper

import com.almanza.kochappi.data.remote.dto.TemplateDetailDto
import com.almanza.kochappi.data.remote.dto.TemplateDto
import com.almanza.kochappi.data.remote.dto.TemplateWithDetailsDto
import com.almanza.kochappi.domain.model.Template
import com.almanza.kochappi.domain.model.TemplateDetail
import com.almanza.kochappi.domain.model.TemplateWithDetails

fun TemplateDto.toDomain(): Template = Template(
    id = id,
    name = name,
    description = description,
)

fun TemplateDetailDto.toDomain(): TemplateDetail = TemplateDetail(
    id = id,
    exerciseId = exerciseId,
    dayOfWeek = dayOfWeek,
    displayOrder = displayOrder,
    sets = sets,
    reps = reps,
)

fun TemplateWithDetailsDto.toDomain(): TemplateWithDetails = TemplateWithDetails(
    id = id,
    name = name,
    description = description,
    details = details.map { it.toDomain() },
)
