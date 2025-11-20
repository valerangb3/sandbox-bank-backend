package domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class JwtTokens(
    val accessToken: String,
    val refreshToken: String
)