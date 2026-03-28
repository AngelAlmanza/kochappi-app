package com.almanza.kochappi.domain.model

data class Template(
    val id: Int,
    val name: String,
    val description: String?,
)

data class TemplateDetail(
    val id: Int,
    val exerciseId: Int,
    val dayOfWeek: Int,
    val displayOrder: Int,
    val sets: Int,
    val reps: Int,
)

data class TemplateWithDetails(
    val id: Int,
    val name: String,
    val description: String?,
    val details: List<TemplateDetail>,
)

data class CreateDetailParams(
    val exerciseId: Int,
    val dayOfWeek: Int,
    val displayOrder: Int,
    val sets: Int,
    val reps: Int,
)
