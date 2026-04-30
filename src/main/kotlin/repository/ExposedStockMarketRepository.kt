package repository

import domain.exception.BankOutOfStockException
import domain.exception.StockNotFoundException
import domain.exception.WalletMissingStockException
import domain.model.AuditLogEntry
import domain.model.StockEntry
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import repository.tables.AuditLogsTable
import repository.tables.BankStocksTable
import repository.tables.WalletStocksTable
import repository.tables.WalletsTable

class ExposedStockMarketRepository : StockMarketRepository {
    override fun buyStock(walletId: String, stockName: String) {
        transaction {
            val bankStock = BankStocksTable
                .selectAll()
                .where { BankStocksTable.stockName eq stockName }
                .forUpdate()
                .singleOrNull() ?: throw StockNotFoundException(stockName)
            val availableInBank = bankStock[BankStocksTable.quantity]
            if (availableInBank <= 0) {
                throw BankOutOfStockException(stockName)
            }
            BankStocksTable.update({ BankStocksTable.stockName eq stockName }) {
                it[quantity] = availableInBank - 1
            }

            val walletExists = WalletsTable
                .selectAll()
                .where { WalletsTable.id eq walletId }.count() > 0
            if (!walletExists) {
                WalletsTable.insert { it[id] = walletId }
            }

            val walletStock = WalletStocksTable
                .selectAll()
                .where { (WalletStocksTable.walletId eq walletId) and (WalletStocksTable.stockName eq stockName) }
                .forUpdate()
                .singleOrNull()
            if (walletStock != null) {
                WalletStocksTable.update({ (WalletStocksTable.walletId eq walletId) and (WalletStocksTable.stockName eq stockName) }) {
                    it[quantity] = walletStock[WalletStocksTable.quantity] + 1
                }
            } else {
                WalletStocksTable.insert {
                    it[this.walletId] = walletId
                    it[this.stockName] = stockName
                    it[quantity] = 1
                }
            }

            AuditLogsTable.insert {
                it[type] = "buy"
                it[this.walletId] = walletId
                it[this.stockName] = stockName
            }
        }
    }

    override fun sellStock(walletId: String, stockName: String) {
        transaction {
            val bankStock = BankStocksTable
                .selectAll()
                .where { BankStocksTable.stockName eq stockName }
                .forUpdate()
                .singleOrNull() ?: throw StockNotFoundException(stockName)

            val walletStock = WalletStocksTable
                .selectAll()
                .where { (WalletStocksTable.walletId eq walletId) and (WalletStocksTable.stockName eq stockName) }
                .forUpdate()
                .singleOrNull() ?: throw WalletMissingStockException(walletId, stockName)

            val availableInWallet = walletStock[WalletStocksTable.quantity]

            if (availableInWallet <= 0) {
                throw WalletMissingStockException(walletId, stockName)
            }

            WalletStocksTable.update({ (WalletStocksTable.walletId eq walletId) and (WalletStocksTable.stockName eq stockName) }) {
                it[quantity] = availableInWallet - 1
            }

            BankStocksTable.update({ BankStocksTable.stockName eq stockName }) {
                it[quantity] = bankStock[BankStocksTable.quantity] + 1
            }

            AuditLogsTable.insert {
                it[type] = "sell"
                it[this.walletId] = walletId
                it[this.stockName] = stockName
            }
        }
    }

    override fun getWallet(walletId: String): Map<String, Int> {
        return transaction {
            WalletStocksTable
                .selectAll()
                .where { WalletStocksTable.walletId eq walletId }
                .associate { it[WalletStocksTable.stockName] to it[WalletStocksTable.quantity] }
        }
    }

    override fun getWalletStockQuantity(walletId: String, stockName: String): Int {
        return transaction {
            WalletStocksTable
                .select(WalletStocksTable.quantity)
                .where { (WalletStocksTable.walletId eq walletId) and (WalletStocksTable.stockName eq stockName) }
                .singleOrNull()?.get(WalletStocksTable.quantity) ?: 0
        }
    }

    override fun getBankStocks(): List<StockEntry> {
        return transaction {
            BankStocksTable
                .selectAll()
                .map { StockEntry(it[BankStocksTable.stockName], it[BankStocksTable.quantity]) }
        }
    }

    override fun setBankStocks(stocks: List<StockEntry>) {
        transaction {
            BankStocksTable.deleteAll()

            stocks.forEach { entry ->
                BankStocksTable.insert {
                    it[stockName] = entry.name
                    it[quantity] = entry.quantity
                }
            }
        }
    }

    override fun getAuditLogs(): List<AuditLogEntry> {
        return transaction {
            AuditLogsTable
                .selectAll()
                .orderBy(AuditLogsTable.id to SortOrder.ASC)
                .map {
                    AuditLogEntry(
                        type = it[AuditLogsTable.type],
                        walletId = it[AuditLogsTable.walletId],
                        stockName = it[AuditLogsTable.stockName]
                    )
                }
        }
    }


}