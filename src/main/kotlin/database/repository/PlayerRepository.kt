package ru.ynovka.database.repository

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.plus
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.upsert
import ru.ynovka.database.DatabaseExecutor
import ru.ynovka.database.table.PlayerTable

object PlayerRepository {

    suspend fun deleteAllWithServer(server: Long) {
        DatabaseExecutor.transaction {
            PlayerTable.deleteWhere { PlayerTable.serverId eq server }
        }
    }
    
    suspend fun incrementCorrect(playerId: Long, serverId: Long) =
        increment(playerId, serverId, PlayerTable.correct)
    
    suspend fun incrementWrong(playerId: Long, serverId: Long) =
        increment(playerId, serverId, PlayerTable.wrong)
    
    suspend fun incrementMath(playerId: Long, serverId: Long) =
        increment(playerId, serverId, PlayerTable.math)
    
    private suspend fun increment(playerId: Long, serverId: Long, column: Column<UInt>) {
        DatabaseExecutor.transaction {
            PlayerTable.upsert(
                keys = arrayOf(PlayerTable.serverId, PlayerTable.playerId),
                onUpdate = { it[column] = column + 1u }
            ) {
                it[PlayerTable.serverId] = serverId
                it[PlayerTable.playerId] = playerId
                it[column] = 1u
            }
        }
    }
    
}