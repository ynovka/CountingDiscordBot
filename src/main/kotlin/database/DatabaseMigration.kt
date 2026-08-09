package ru.ynovka.database

import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import ru.ynovka.database.DataBase.Companion.mariadb
import ru.ynovka.database.table.PlayerTable
import ru.ynovka.database.table.ServerTable

object DatabaseMigration {
    
    fun migrate() {
        val tables = arrayOf(
            PlayerTable,
            ServerTable
        )
        
        transaction(mariadb) {
            
            TransactionManager.current().db.dialectMetadata.resetCaches()
            
            SchemaUtils.createStatements(*tables).forEach { statement -> exec(statement) }
            
            TransactionManager.current().db.dialectMetadata.resetCaches()
            
            SchemaUtils.addMissingColumnsStatements(*tables).forEach { statement -> exec(statement) }
            
            TransactionManager.current().db.dialectMetadata.resetCaches()
        }
    }
}
