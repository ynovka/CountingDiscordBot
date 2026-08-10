package ru.ynovka.database.repository

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.plus
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.upsert
import ru.ynovka.database.DatabaseExecutor
import ru.ynovka.database.model.toServerModel
import ru.ynovka.database.table.ServerTable

object ServerRepository {
    
    suspend fun getServers(): Set<Long> =
        DatabaseExecutor.transaction {
            ServerTable
                .select(ServerTable.id)
                .map { it[ServerTable.id] }
                .toSet()
        }
    
    suspend fun addServer(server: Long) {
        DatabaseExecutor.transaction {
            ServerTable.upsert {
                it[ServerTable.id] = server
            }
        }
    }
    
    suspend fun removeServer(server: Long) {
        DatabaseExecutor.transaction {
            ServerTable.deleteWhere { ServerTable.id eq server }
        }
    }
    
    suspend fun getServer(server: Long) =
        DatabaseExecutor.transaction {
            ServerTable
                .select(
                    ServerTable.id,
                    ServerTable.channelId,
                    ServerTable.lastSender,
                    ServerTable.currentScore,
                    ServerTable.joinAt
                )
                .where { ServerTable.id eq server }
                .singleOrNull()
                ?.toServerModel()
        }
    
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
    
    suspend fun updateSender(server: Long, sender: Long) {
        DatabaseExecutor.transaction {
            ServerTable.update(
                where = { ServerTable.id eq server },
                limit = 1
            ) {
                it[ServerTable.lastSender] = sender
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
    
    suspend fun incrementScore(server: Long) {
        DatabaseExecutor.transaction {
            ServerTable.update(
                where = { ServerTable.id eq server },
                limit = 1
            ) {
                it[ServerTable.currentScore] = ServerTable.currentScore + 1u
            }
        }
    }
    
}