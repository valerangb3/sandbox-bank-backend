package repository

import domain.model.auth.JwtTokens

interface TokenRepository {
    suspend fun saveToken(login: String, tokens: JwtTokens)
    suspend fun getUserByToken(token: String): String?
}