package com.almanza.kochappi.data.mapper

import com.almanza.kochappi.data.remote.dto.AuthResponseDto
import com.almanza.kochappi.data.remote.dto.MessageResponseDto
import com.almanza.kochappi.domain.model.MessageResponse
import com.almanza.kochappi.domain.model.Session
import com.almanza.kochappi.domain.model.UserRole

fun AuthResponseDto.toDomain(): Session = Session(
    accessToken = accessToken,
    refreshToken = refreshToken,
    userId = user.id,
    role = UserRole.valueOf(user.role.uppercase()),
    displayName = user.name,
    email = user.email,
)

fun MessageResponseDto.toDomain(): MessageResponse = MessageResponse(message = message)
