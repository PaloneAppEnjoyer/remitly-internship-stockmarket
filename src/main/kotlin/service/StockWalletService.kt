package service

import domain.model.AuditLogEntry
import domain.model.StockEntry

interface StockWalletService {
    //wallet
    fun executeTrade(walletId:String, stockName: String, type:String)
    fun getWallet(walletId: String): Map<String, Int>
    fun getWalletStockQuantity(walletId: String, stockName: String): Int
    //stocks
    fun getBankStocks(): List<StockEntry>
    fun setBankStocks(stocks: List<StockEntry>)
    //logs
    fun getAuditLogs():List<AuditLogEntry>
}