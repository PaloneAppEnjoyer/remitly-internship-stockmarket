package repository.tables

import org.jetbrains.exposed.v1.core.Table

object WalletStocksTable : Table("wallet_stocks") {
    val walletId = varchar("wallet_id", 255).references(WalletsTable.id)
    val stockName = varchar("stock_name", 255)
    val quantity = integer("quantity")

    override val primaryKey = PrimaryKey(walletId, stockName)
}