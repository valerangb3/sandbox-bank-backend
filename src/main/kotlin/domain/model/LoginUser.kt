package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginUser(
    val login: String,
    val password: String
)
