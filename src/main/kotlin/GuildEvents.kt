package ru.ynovka

import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import ru.ynovka.Main.Companion.jda
import ru.ynovka.database.service.ServerService

object GuildEvents {
    fun register() {
        jda.listener<ReadyEvent> { e ->
            val servers = e.jda.guilds.map { it.idLong }.toSet()
            println("Registered ${servers.size} servers")
            
            ServerService.updateServers(servers)
        }
        
        jda.listener<GuildJoinEvent> { e ->
            val server = e.guild.idLong
            println("Added new server $server")
            
            ServerService.addServer(server)
        }
        
        jda.listener<GuildLeaveEvent> { e ->
            val server = e.guild.idLong
            println("Removed server $server")
            
            ServerService.removeServer(server)
        }
    }
}