package service

import domain.exception.InvalidTradeTypeException
import domain.model.AuditLogEntry
import domain.model.StockEntry
import repository.StockMarketRepository

class StockWalletServiceImpl(private val repository: StockMarketRepository) : StockWalletService {
    override fun executeTrade(walletId: String, stockName: String, type: String) {
        when (type) {
            "buy" -> repository.buyStock(walletId, stockName)
            "sell" -> repository.sellStock(walletId, stockName)
            else -> throw InvalidTradeTypeException(type)
        }
    }

    override fun getWallet(walletId: String): Map<String, Int> {
        return repository.getWallet(walletId)
    }

    override fun getWalletStockQuantity(walletId: String, stockName: String): Int {
        return repository.getWalletStockQuantity(walletId, stockName)
    }

    override fun getBankStocks(): List<StockEntry> {
        return repository.getBankStocks()
    }

    override fun setBankStocks(stocks: List<StockEntry>) {
        repository.setBankStocks(stocks)
    }

    override fun getAuditLogs(): List<AuditLogEntry> {
        return repository.getAuditLogs()
    }
}