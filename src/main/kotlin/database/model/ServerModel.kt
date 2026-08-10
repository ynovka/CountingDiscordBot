package ru.ynovka.database.model

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ResultRow
import ru.ynovka.database.table.PlayerTable
import ru.ynovka.database.table.ServerTable
import kotlin.time.Instant

data class ServerModel(
    val channelId: Long?,
    val lastSender: Long?,
    val currentScore: UInt,
    val joinAt: Instant
)

enum class PlayerStat(val column: Column<UInt>) {
    CORRECT(PlayerTable.correct),
    WRONG(PlayerTable.wrong),
    MATH(PlayerTable.math)
}

fun ResultRow.toServerModel(): ServerModel {
    return ServerModel(
        channelId = this[ServerTable.channelId],
        lastSender = this[ServerTable.lastSender],
        currentScore = this[ServerTable.currentScore],
        joinAt = this[ServerTable.joinAt]
    )
}
