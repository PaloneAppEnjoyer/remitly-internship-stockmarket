package repository.tables

import org.jetbrains.exposed.v1.core.Table

object AuditLogsTable : Table("audit_logs") {
    val id = integer("id").autoIncrement()
    val type = varchar("type", 10)
    val walletId = varchar("wallet_id", 255)
    val stockName = varchar("stock_name", 255)
    
    override val primaryKey = PrimaryKey(id)
}