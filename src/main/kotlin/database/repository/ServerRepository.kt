package ru.ynovka.database.repository

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import ru.ynovka.database.DatabaseExecutor
import ru.ynovka.database.table.ServerTable

object ServerRepository {
    
    suspend fun getServers(): Set<Long> =
        DatabaseExecutor.transaction {
            ServerTable.selectAll()
                .map { it[ServerTable.id] }
                .toSet()
        }
    
    suspend fun addServer(server: Long) {
        DatabaseExecutor.transaction {
            ServerTable.insert {
                it[ServerTable.id] = server
            }
        }
    }
    
    suspend fun removeServer(server: Long) {
        DatabaseExecutor.transaction {
            ServerTable.deleteWhere { ServerTable.id eq server }
        }
    }

}