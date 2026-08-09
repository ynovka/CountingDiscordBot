package ru.ynovka

import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import ru.ynovka.Main.Companion.jda

object MessageEvents {
    fun register() {
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