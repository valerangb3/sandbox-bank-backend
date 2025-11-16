package routing.model.response

import kotlinx.serialization.Serializable

@Serializable
data class JwtTokensResponse(
    val accessToken: String,
    val refreshToken: String
)
