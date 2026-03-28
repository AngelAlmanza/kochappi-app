package com.almanza.kochappi.domain.repository

import com.almanza.kochappi.domain.model.CreateDetailParams
import com.almanza.kochappi.domain.model.Template
import com.almanza.kochappi.domain.model.TemplateDetail
import com.almanza.kochappi.domain.model.TemplateWithDetails

interface TemplateRepository {
    suspend fun getTemplates(): List<Template>
    suspend fun getTemplate(id: Int): TemplateWithDetails
    suspend fun createTemplate(
        name: String,
        description: String?,
        details: List<CreateDetailParams> = emptyList(),
    ): TemplateWithDetails
    suspend fun updateTemplate(id: Int, name: String, description: String?): Template
    suspend fun deleteTemplate(id: Int)
    suspend fun addDetail(
        templateId: Int,
        exerciseId: Int,
        dayOfWeek: Int,
        displayOrder: Int,
        sets: Int,
        reps: Int,
    ): TemplateDetail
    suspend fun deleteDetail(templateId: Int, detailId: Int)
}
