package ru.ynovka.database.table

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object ServerTable : Table("server") {
    
    val id = long("id")
    
    val channelId = long("channel_id").nullable()
    
    val lastSender = long("last_sender").nullable()
    
    val currentScore = uinteger("current_score").default(0u)
    
    val bestScore = uinteger("best_score").default(0u)
    
    val joinAt = timestamp("join_at").defaultExpression(CurrentTimestamp)
    
    override val primaryKey = PrimaryKey(id)
}