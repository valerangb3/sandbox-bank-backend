package com.example.repository

import repository.db.UserDAO
import com.example.repository.db.suspendTransaction
import com.example.domain.model.RegisterUser
import com.example.repository.db.userDaoToModel
import com.example.repository.model.User
import com.example.security.md5
import org.jetbrains.exposed.v1.core.eq
import repository.db.UserTable

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
                    accessToken = ""
                    refreshToken = ""
                }
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun userByLogin(login: String): User? {
        return suspendTransaction {
            UserDAO
                .find { (UserTable.login eq login) }
                .map(::userDaoToModel)
                .firstOrNull()
        }
    }

    override suspend fun userByToken(token: String): User? {
        return suspendTransaction {
            UserDAO
                .find { (UserTable.refreshToken eq token) }
                .map(::userDaoToModel)
                .firstOrNull()
        }
    }

    override suspend fun isPasswordEquals(password: String, user: User): Boolean {
        return md5(password) == user.passwordHash
    }
}