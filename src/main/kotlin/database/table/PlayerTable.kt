package ru.ynovka.database.table

import org.jetbrains.exposed.v1.core.Table

object PlayerTable : Table("player") {
    
    val playerId = long("player_id")
    
    val serverId = long("server_id").references(ServerTable.id)
    
    var correct = uinteger("correct").default(0u)
    var wrong = uinteger("wrong").default(0u)
    var math = uinteger("math").default(0u)
    
    override val primaryKey = PrimaryKey(serverId, playerId)
}