package ru.ynovka.database.repository

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.plus
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.insertIgnore
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.upsert
import ru.ynovka.database.DatabaseExecutor
import ru.ynovka.database.model.PlayerModel
import ru.ynovka.database.model.PlayerStat
import ru.ynovka.database.model.toPlayerModel
import ru.ynovka.database.table.PlayerTable
import ru.ynovka.database.table.ServerTable

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
            ServerTable.insertIgnore {
                it[ServerTable.id] = serverId
            }
            
            val updated = PlayerTable.update(
                where = { (PlayerTable.serverId eq serverId) and (PlayerTable.playerId eq playerId) },
                limit = 1
            ) {
                it[column] = column + 1u
            }
            
            if (updated == 0) {
                PlayerTable.insert {
                    it[PlayerTable.serverId] = serverId
                    it[PlayerTable.playerId] = playerId
                    it[column] = 1u
                }
            }
        }
    }
    
    suspend fun getTop(serverId: Long, playerId: Long, stat: PlayerStat): Map<Int, PlayerModel> {
        return DatabaseExecutor.transaction {
            val top = PlayerTable
                .selectAll()
                .where { PlayerTable.serverId eq serverId }
                .orderBy(stat.column, SortOrder.DESC)
                .limit(5)
                .map { it.toPlayerModel() }
            
            val result = top.withIndex().associate { (index, model) -> index + 1 to model }
            
            if (top.any { it.playerId == playerId }) return@transaction result
            
            val player = PlayerTable
                .selectAll()
                .where { (PlayerTable.serverId eq serverId) and (PlayerTable.playerId eq playerId) }
                .singleOrNull()
                ?.toPlayerModel() ?: return@transaction result
            
            val playerValue = when (stat) {
                PlayerStat.CORRECT -> player.correct
                PlayerStat.WRONG -> player.wrong
                PlayerStat.MATH -> player.math
            }
            
            val place = PlayerTable
                .selectAll()
                .where { (PlayerTable.serverId eq serverId) and (stat.column greater playerValue) }
                .count()
                .toInt() + 1
            
            result + (place to player)
        }
    }
    
}