package com.almanza.kochappi.data.repository

import com.almanza.kochappi.data.mapper.toDomain
import com.almanza.kochappi.data.remote.api.TemplateApi
import com.almanza.kochappi.data.remote.dto.AddDetailRequest
import com.almanza.kochappi.data.remote.dto.CreateDetailRequestItem
import com.almanza.kochappi.data.remote.dto.CreateTemplateRequest
import com.almanza.kochappi.data.remote.dto.UpdateTemplateRequest
import com.almanza.kochappi.domain.model.CreateDetailParams
import com.almanza.kochappi.domain.model.Template
import com.almanza.kochappi.domain.model.TemplateDetail
import com.almanza.kochappi.domain.model.TemplateWithDetails
import com.almanza.kochappi.domain.repository.TemplateRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateRepositoryImpl @Inject constructor(
    private val templateApi: TemplateApi,
) : TemplateRepository {

    override suspend fun getTemplates(): List<Template> =
        templateApi.getTemplates().map { it.toDomain() }

    override suspend fun getTemplate(id: Int): TemplateWithDetails =
        templateApi.getTemplate(id).toDomain()

    override suspend fun createTemplate(
        name: String,
        description: String?,
        details: List<CreateDetailParams>,
    ): TemplateWithDetails = templateApi.createTemplate(
        CreateTemplateRequest(
            name = name,
            description = description,
            details = details.takeIf { it.isNotEmpty() }?.map {
                CreateDetailRequestItem(
                    exerciseId = it.exerciseId,
                    dayOfWeek = it.dayOfWeek,
                    displayOrder = it.displayOrder,
                    sets = it.sets,
                    reps = it.reps,
                )
            },
        )
    ).toDomain()

    override suspend fun updateTemplate(
        id: Int,
        name: String,
        description: String?,
    ): Template = templateApi.updateTemplate(
        id,
        UpdateTemplateRequest(name = name, description = description),
    ).toDomain()

    override suspend fun deleteTemplate(id: Int) =
        templateApi.deleteTemplate(id)

    override suspend fun addDetail(
        templateId: Int,
        exerciseId: Int,
        dayOfWeek: Int,
        displayOrder: Int,
        sets: Int,
        reps: Int,
    ): TemplateDetail = templateApi.addDetail(
        templateId,
        AddDetailRequest(
            exerciseId = exerciseId,
            dayOfWeek = dayOfWeek,
            displayOrder = displayOrder,
            sets = sets,
            reps = reps,
        ),
    ).toDomain()

    override suspend fun deleteDetail(templateId: Int, detailId: Int) =
        templateApi.deleteDetail(templateId, detailId)
}
