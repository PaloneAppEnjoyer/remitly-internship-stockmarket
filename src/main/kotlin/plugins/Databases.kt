package plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import repository.tables.AuditLogsTable
import repository.tables.BankStocksTable
import repository.tables.WalletStocksTable
import repository.tables.WalletsTable

suspend fun Application.configureDatabase() {
    val config = HikariConfig().apply {
        jdbcUrl = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/stockmarket_db"
        driverClassName = "org.postgresql.Driver"
        username = System.getenv("DB_USER") ?: "admin"
        password = System.getenv("DB_PASSWORD") ?: "admin_password"

        maximumPoolSize = 10
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_READ_COMMITTED"
        addDataSourceProperty("cachePrepStmts", "true")
        addDataSourceProperty("prepStmtCacheSize", "250")
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        validate()
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
    transaction {
        SchemaUtils.create(
            WalletsTable,
            BankStocksTable,
            WalletStocksTable,
            AuditLogsTable
        )
    }
}
