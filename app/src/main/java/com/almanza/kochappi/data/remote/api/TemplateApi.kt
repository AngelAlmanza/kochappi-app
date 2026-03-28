package com.almanza.kochappi.data.remote.api

import com.almanza.kochappi.data.remote.dto.AddDetailRequest
import com.almanza.kochappi.data.remote.dto.CreateTemplateRequest
import com.almanza.kochappi.data.remote.dto.TemplateDetailDto
import com.almanza.kochappi.data.remote.dto.TemplateDto
import com.almanza.kochappi.data.remote.dto.TemplateWithDetailsDto
import com.almanza.kochappi.data.remote.dto.UpdateTemplateRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TemplateApi {

    @GET("templates")
    suspend fun getTemplates(): List<TemplateDto>

    @GET("templates/{id}")
    suspend fun getTemplate(@Path("id") id: Int): TemplateWithDetailsDto

    @POST("templates")
    suspend fun createTemplate(@Body body: CreateTemplateRequest): TemplateWithDetailsDto

    @PUT("templates/{id}")
    suspend fun updateTemplate(
        @Path("id") id: Int,
        @Body body: UpdateTemplateRequest,
    ): TemplateDto

    @DELETE("templates/{id}")
    suspend fun deleteTemplate(@Path("id") id: Int)

    @POST("templates/{id}/details")
    suspend fun addDetail(
        @Path("id") templateId: Int,
        @Body body: AddDetailRequest,
    ): TemplateDetailDto

    @DELETE("templates/{id}/details/{detailId}")
    suspend fun deleteDetail(
        @Path("id") templateId: Int,
        @Path("detailId") detailId: Int,
    )
}
