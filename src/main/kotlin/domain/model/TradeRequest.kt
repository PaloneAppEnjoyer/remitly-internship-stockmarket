package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TradeRequest(
    val type: String
)