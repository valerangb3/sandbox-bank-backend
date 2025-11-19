package domain.model.auth

data class JwtTokens(
    val accessToken: String,
    val refreshToken: String
)