package repository.tables

import org.jetbrains.exposed.v1.core.Table

object WalletsTable : Table("wallets") {
    val id = varchar("id", 255)
    
    override val primaryKey = PrimaryKey(id)
}