package ru.ynovka

import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import ru.ynovka.Main.Companion.jda

object GuildEvents {
    fun register() {
        jda.listener<ReadyEvent> { e ->
            val servers = e.jda.guilds.map { it.idLong }
            println("Registered ${servers.size} servers")
            
            // todo db service updateServers(servers)
            //  - если сервера нету в БД - добавляем, если сервера нету в списке - удаляем из БД + удаляем Players
        }
        
        jda.listener<GuildJoinEvent> { e ->
            val server = e.guild.idLong
            println("Added new server $server")
            
            // todo db service addServer(server)
            //  - добавляем в БД
        }
        
        jda.listener<GuildLeaveEvent> { e ->
            val server = e.guild.idLong
            println("Removed server $server")
            
            // todo db service removeServer(server)
            //  - удаляем из БД
        }
    }
}