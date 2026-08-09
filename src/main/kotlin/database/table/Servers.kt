package ru.ynovka.database.table

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object Servers : Table("servers") {
    
    val id = long("id")
    
    var channelId = long("channel_id").nullable()
    
    var currentScore = uinteger("current_score").default(0u)
    
    val joinAt = timestamp("join_at").defaultExpression(CurrentTimestamp)
    
    override val primaryKey = PrimaryKey(id)
}