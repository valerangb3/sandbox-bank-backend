package service

import domain.model.auth.JwtTokens

enum class AuthError {
    INVALID_PASSWORD, USER_NOT_EXIST
}

sealed interface AuthResult {
    data class Success(val tokes: JwtTokens) : AuthResult
    data class Error(val authError: AuthError) : AuthResult
}