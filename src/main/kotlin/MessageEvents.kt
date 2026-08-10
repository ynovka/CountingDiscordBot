package ru.ynovka

import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.objecthunter.exp4j.ExpressionBuilder
import ru.ynovka.Main.Companion.jda
import ru.ynovka.database.service.PlayerService
import ru.ynovka.database.service.ServerService
import ru.ynovka.messages.ArithmeticExceptionMessage
import ru.ynovka.messages.MessageDeleteMessage
import ru.ynovka.messages.MessageUpdateMessage
import ru.ynovka.messages.NonArithmeticExceptionMessage
import java.util.concurrent.TimeUnit

object MessageEvents {
    fun register() {
        jda.listener<MessageReceivedEvent> { e ->
            val serverId = e.guild.idLong
            val playerId = e.author.idLong
            val channel = e.channel
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
            } catch (_: IllegalArgumentException) {
                channel.sendMessage(
                    NonArithmeticExceptionMessage.message
                ).queueAfter(5L, TimeUnit.SECONDS) { botMessage ->
                    botMessage.delete().queue()
                    message.delete().queue()
                }
            } catch (_: ArithmeticException) {
                channel.sendMessage(
                    ArithmeticExceptionMessage.message
                ).queueAfter(5L, TimeUnit.SECONDS) { botMessage ->
                    botMessage.delete().queue()
                    message.delete().queue()
                }
            }
        }
        
        jda.listener<MessageDeleteEvent> { e ->
            val serverId = e.guild.idLong
            val score = ServerService.getCurrentScore(serverId) ?: return@listener
            val channel = e.channel
            
            channel.sendMessage(
                MessageDeleteMessage.getMessage(score)
            ).queue()
        }
        
        jda.listener<MessageUpdateEvent> { e ->
            val serverId = e.guild.idLong
            val score = ServerService.getCurrentScore(serverId) ?: return@listener
            val channel = e.channel
            
            channel.sendMessage(
                MessageUpdateMessage.getMessage(score)
            ).queue()
        }
    }
}