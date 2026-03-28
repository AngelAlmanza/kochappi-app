package com.almanza.kochappi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TemplateDto(
    val id: Int,
    val name: String,
    val description: String? = null,
)

@Serializable
data class TemplateDetailDto(
    val id: Int,
    val exerciseId: Int,
    val dayOfWeek: Int,
    val displayOrder: Int,
    val sets: Int,
    val reps: Int,
)

@Serializable
data class TemplateWithDetailsDto(
    val id: Int,
    val name: String,
    val description: String? = null,
    val details: List<TemplateDetailDto> = emptyList(),
)

@Serializable
data class CreateTemplateRequest(
    val name: String,
    val description: String? = null,
    val details: List<CreateDetailRequestItem>? = null,
)

@Serializable
data class CreateDetailRequestItem(
    val exerciseId: Int,
    val dayOfWeek: Int,
    val displayOrder: Int,
    val sets: Int,
    val reps: Int,
)

@Serializable
data class UpdateTemplateRequest(
    val name: String,
    val description: String? = null,
)

@Serializable
data class AddDetailRequest(
    val exerciseId: Int,
    val dayOfWeek: Int,
    val displayOrder: Int,
    val sets: Int,
    val reps: Int,
)
