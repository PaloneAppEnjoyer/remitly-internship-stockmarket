package repository.tables

import org.jetbrains.exposed.v1.core.Table

object BankStocksTable : Table("bank_stocks") {
    val stockName = varchar("stock_name", 255)
    val quantity = integer("quantity")
    
    override val primaryKey = PrimaryKey(stockName)
}