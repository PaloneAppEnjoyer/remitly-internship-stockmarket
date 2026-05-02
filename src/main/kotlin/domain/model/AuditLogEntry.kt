package domain.model

import com.fasterxml.jackson.annotation.JsonProperty

data class AuditLogResponse(
    val log: List<AuditLogEntry>
)

data class AuditLogEntry(
    val type: String, // "buy" or "sell"
    @JsonProperty("wallet_id") val walletId: String,
    @JsonProperty("stock_name") val stockName: String
)