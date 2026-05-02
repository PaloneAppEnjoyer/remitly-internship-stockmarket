package service

import domain.exception.InvalidTradeTypeException
import domain.model.AuditLogEntry
import domain.model.StockEntry
import org.junit.Test
import repository.StockMarketRepository
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class StockWalletServiceImplTest {

    private val mockRepository = object : StockMarketRepository {
        var buyStockCalled = false
        var sellStockCalled = false
        var setBankStocksCalled = false
        var passedStocks: List<StockEntry> = emptyList()

        override fun buyStock(walletId: String, stockName: String) {
            buyStockCalled = true
        }

        override fun sellStock(walletId: String, stockName: String) {
            sellStockCalled = true
        }

        override fun getWallet(walletId: String): Map<String, Int> {
            return mapOf("stockA" to 5)
        }

        override fun getWalletStockQuantity(walletId: String, stockName: String): Int {
            return 10
        }

        override fun getBankStocks(): List<StockEntry> {
            return listOf(StockEntry("bankStock", 100))
        }

        override fun setBankStocks(stocks: List<StockEntry>) {
            setBankStocksCalled = true
            passedStocks = stocks
        }

        override fun getAuditLogs(): List<AuditLogEntry> {
            return listOf(AuditLogEntry("buy", "wallet1", "stockB"))
        }
    }

    private val service = StockWalletServiceImpl(mockRepository)

    @Test
    fun `executeTrade with buy calls repository buyStock`() {
        service.executeTrade("w1", "s1", "buy")
        assertTrue(mockRepository.buyStockCalled)
    }

    @Test
    fun `executeTrade with sell calls repository sellStock`() {
        service.executeTrade("w1", "s1", "sell")
        assertTrue(mockRepository.sellStockCalled)
    }

    @Test
    fun `executeTrade with invalid type throws exception`() {
        assertFailsWith<InvalidTradeTypeException> {
            service.executeTrade("w1", "s1", "invalid")
        }
    }

    @Test
    fun `getWallet returns data from repository`() {
        val result = service.getWallet("w1")
        assertEquals(mapOf("stockA" to 5), result)
    }

    @Test
    fun `getWalletStockQuantity returns data from repository`() {
        val result = service.getWalletStockQuantity("w1", "s1")
        assertEquals(10, result)
    }
    
    @Test
    fun `getBankStocks returns data from repository`() {
        val result = service.getBankStocks()
        assertEquals(listOf(StockEntry("bankStock", 100)), result)
    }
    
    @Test
    fun `setBankStocks passes data to repository`() {
        val stocks = listOf(StockEntry("newStock", 50))
        service.setBankStocks(stocks)
        assertTrue(mockRepository.setBankStocksCalled)
        assertEquals(stocks, mockRepository.passedStocks)
    }
    
    @Test
    fun `getAuditLogs returns data from repository`() {
        val result = service.getAuditLogs()
        assertEquals(listOf(AuditLogEntry("buy", "wallet1", "stockB")), result)
    }
}