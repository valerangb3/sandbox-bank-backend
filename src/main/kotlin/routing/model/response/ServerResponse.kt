package routing.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse(
    val status: Int,
    val message: String,
    val description: String,
    val data: JwtTokensResponse?
)
