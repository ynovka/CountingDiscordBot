package ru.ynovka.database.repository

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
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
    
    suspend fun getChannel(server: Long) =
        DatabaseExecutor.transaction {
            ServerTable.select(ServerTable.channelId)
                .where { ServerTable.id eq server }
                .singleOrNull()
        }?.let { it[ServerTable.channelId] }
    
    suspend fun setChannel(server: Long, channel: Long) {
        DatabaseExecutor.transaction {
            ServerTable.update(
                where = { ServerTable.id eq server },
                limit = 1
            ) {
                it[ServerTable.channelId] = channel
            }
        }
    }
    
    suspend fun resetScore(server: Long) {
        DatabaseExecutor.transaction {
            ServerTable.update(
                where = { ServerTable.id eq server },
                limit = 1
            ) {
                it[ServerTable.currentScore] = 0u
            }
        }
    }
}