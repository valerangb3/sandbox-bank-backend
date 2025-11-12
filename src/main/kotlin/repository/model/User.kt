package com.example.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val login: String,
    val email: String,
    @SerialName("password_hash")
    val passwordHash: String,
    val phone: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("first_name")
    val firstName: String?,
    val patronymic: String?
)
