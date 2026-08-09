package ru.ynovka.database.repository

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import ru.ynovka.database.DatabaseExecutor
import ru.ynovka.database.table.PlayerTable

object PlayerRepository {

    suspend fun deleteAllWithServer(server: Long) {
        DatabaseExecutor.transaction {
            PlayerTable.deleteWhere { PlayerTable.serverId eq server }
        }
    }

}