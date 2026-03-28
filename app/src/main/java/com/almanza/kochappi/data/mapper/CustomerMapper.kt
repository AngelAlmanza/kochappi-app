package com.almanza.kochappi.data.mapper

import com.almanza.kochappi.data.remote.dto.CustomerDto
import com.almanza.kochappi.data.remote.dto.RegisterResponseDto
import com.almanza.kochappi.data.remote.dto.UserListDto
import com.almanza.kochappi.domain.model.Customer
import com.almanza.kochappi.domain.model.User
import com.almanza.kochappi.domain.model.UserRole

fun CustomerDto.toDomain(): Customer = Customer(
    id = id,
    name = name,
    birthdate = birthdate,
)

fun UserListDto.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    role = UserRole.valueOf(role.uppercase()),
)

fun RegisterResponseDto.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    role = UserRole.valueOf(role.uppercase()),
)
