package com.example.repository

import com.example.domain.model.RegisterUser
import com.example.repository.model.User

interface UserRepository {
    suspend fun create(user: RegisterUser): Boolean
    suspend fun userByLogin(login: String): User
}