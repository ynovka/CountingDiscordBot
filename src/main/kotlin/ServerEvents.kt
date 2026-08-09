package ru.ynovka

import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import ru.ynovka.Main.Companion.jda

object ServerEvents {
    fun register() {
        jda.listener<ReadyEvent> { e ->
            val servers = e.jda.guilds.map { it.idLong }
            println("Bot is ready")
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
        
        jda.listener<MessageReceivedEvent> { e ->
            val server = e.guild.idLong
            val content = e.message.contentRaw
            
            // todo
        }
        
        jda.listener<MessageDeleteEvent> { e ->
            val server = e.guild.idLong
            
            // todo Пишем в чат -
            //  "Кто то решил схитрить и удалил сообщение!",
            //  "Как же хорошо что я веду счёт"
            //  "Эм, мы остановились на **X**"
        }
        
        jda.listener<MessageUpdateEvent> { e ->
            val server = e.guild.idLong
            
            // todo Пишем в чат -
            //  "Кто то решил схитрить и изменил своё сообщение!",
            //  "Как же хорошо что я веду счёт"
            //  "Эм, мы остановились на **X**"
        }
    }
}