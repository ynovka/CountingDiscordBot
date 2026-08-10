package ru.ynovka

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.objecthunter.exp4j.ExpressionBuilder
import ru.ynovka.Main.Companion.jda
import ru.ynovka.database.service.PlayerService
import ru.ynovka.database.service.ServerService

object MessageEvents {
    fun register() {
        jda.listener<MessageReceivedEvent> { e ->
            val serverId = e.guild.idLong
            val playerId = e.author.idLong
            val message = e.message
            val content = message.contentRaw
            
            val server = ServerService.getServer(serverId) ?: return@listener
            val serverChannelId = server.channelId ?: return@listener
            
            if (serverChannelId != e.channel.idLong) return@listener
            
            try {
                val strInt = content.toUIntOrNull()
                val strMath = ExpressionBuilder(content).build().evaluate().toUInt()
                
                val isMath = strInt != strMath
                val result = if (isMath) strMath else strInt
                
                if (result == server.currentScore + 1u) {
                    message.addReaction(Emoji.fromFormatted(":abacus:")).queue()
                    
                    ServerService.incrementScore(serverId)
                    PlayerService.incrementCorrect(playerId, serverId)
                    if (isMath) PlayerService.incrementMath(playerId, serverId)
                } else {
                    message.addReaction(Emoji.fromFormatted(":boom:")).queue()
                    
                    PlayerService.incrementWrong(playerId, serverId)
                }
            } catch (e: IllegalArgumentException) {
                // Сюда код попадет, если в строке текст, неизвестные буквы или синтаксические ошибки
                
                
            } catch (e: ArithmeticException) {
                // Сюда код попадет, например, при делении на ноль (если это запрещено настройками)
                
                
            }
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