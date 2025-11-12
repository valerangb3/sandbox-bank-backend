package com.example.repository

import com.example.repository.db.UserDAO
import com.example.repository.db.suspendTransaction
import com.example.domain.model.RegisterUser
import com.example.repository.model.User
import com.example.security.md5

class UserRepositoryImpl : UserRepository {
    override suspend fun create(user: RegisterUser): Boolean {
        return try {
            suspendTransaction {
                UserDAO.Companion.new {
                    login = user.login
                    lastName = user.lastName
                    firstName = user.firstName
                    passwordHash = md5(user.password)
                    email = user.email
                    patronymic = ""
                    phone = ""
                }
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun userByLogin(login: String): User {
        TODO("Not yet implemented")
    }
}