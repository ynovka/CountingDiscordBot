package ru.ynovka.database.model

import org.jetbrains.exposed.v1.core.ResultRow
import ru.ynovka.database.table.ServerTable
import kotlin.time.Instant

data class ServerModel(
    val channelId: Long?,
    val currentScore: UInt,
    val joinAt: Instant
) {
    fun withChannelId(channelId: Long) = copy(channelId = channelId)
}

fun ResultRow.toServerModel(): ServerModel {
    return ServerModel(
        channelId = this[ServerTable.channelId],
        currentScore = this[ServerTable.currentScore],
        joinAt = this[ServerTable.joinAt]
    )
}
