package domain.model.auth

data class JwtTokens(
    val accessToken: String,
    val refreshToken: String
)

data class Tokens<T>(val data: T)