package repository

import com.example.domain.model.LoginUser
import domain.model.auth.Tokens

interface TokenRepository<T> {
    suspend fun refreshToken(user: LoginUser): Tokens<T>
    suspend fun getUserByToken(token: String): String?
}