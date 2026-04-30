package repository

import domain.model.AuditLogEntry
import domain.model.StockEntry

interface StockMarketRepository {
    fun buyStock(walletId: String, stockName: String)
    fun sellStock(walletId: String, stockName: String)

    fun getWallet(walletId: String):Map<String,Int>
    fun getWalletStockQuantity(walletId: String, stockName: String):Int

    fun getBankStocks(): List<StockEntry>
    fun setBankStocks(stocks: List<StockEntry>)

    fun getAuditLogs(): List<AuditLogEntry>

}