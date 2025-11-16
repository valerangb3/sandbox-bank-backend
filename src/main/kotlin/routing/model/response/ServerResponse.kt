package routing.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse<T>(
    val status: Int,
    val message: String,
    val data: T
)
