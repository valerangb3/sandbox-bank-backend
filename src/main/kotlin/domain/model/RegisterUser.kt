package com.example.domain.model

import kotlinx.serialization.Serializable

// 1) для регистрации используются все поля
// 2) для auth используются login и password
@Serializable
data class RegisterUser(
    val login: String,
    val email: String,
    val password: String,
    val lastName: String,
    val firstName: String,
)
