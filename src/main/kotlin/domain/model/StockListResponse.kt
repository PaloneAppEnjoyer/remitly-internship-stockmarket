package domain.model

data class StockListResponse(
    val stocks: List<StockEntry>
)

data class StockListRequest(
    val stocks: List<StockEntry>
)

data class StockEntry(
    val name: String,
    val quantity: Int
)

data class WalletResponse(
    val id: String,
    val stocks: List<StockEntry>
)
