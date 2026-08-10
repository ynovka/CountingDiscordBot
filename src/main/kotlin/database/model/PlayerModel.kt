package ru.ynovka.database.model

import org.jetbrains.exposed.v1.core.ResultRow
import ru.ynovka.database.table.PlayerTable

data class PlayerModel(
    val playerId: Long,
    val serverId: Long,
    val correct: UInt,
    val wrong: UInt,
    val math: UInt
)

fun ResultRow.toPlayerModel(): PlayerModel {
    return PlayerModel(
        playerId = this[PlayerTable.playerId],
        serverId = this[PlayerTable.serverId],
        correct = this[PlayerTable.correct],
        wrong = this[PlayerTable.wrong],
        math = this[PlayerTable.math]
    )
}