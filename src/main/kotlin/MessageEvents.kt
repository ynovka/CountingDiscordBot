package ru.ynovka

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.SendDefaults.content
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.objecthunter.exp4j.ExpressionBuilder
import ru.ynovka.Main.Companion.jda
import ru.ynovka.database.model.ServerModel
import ru.ynovka.database.service.PlayerService
import ru.ynovka.database.service.ServerService
import ru.ynovka.database.table.PlayerTable.playerId
import ru.ynovka.database.table.PlayerTable.serverId
import ru.ynovka.messages.ArithmeticExceptionMessage
import ru.ynovka.messages.MessageDeleteMessage
import ru.ynovka.messages.MessageUpdateMessage
import ru.ynovka.messages.NonArithmeticExceptionMessage
import ru.ynovka.messages.SenderRepeatMessage
import ru.ynovka.messages.WrongNumberMessage
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object MessageEvents {
    
    private val ignoreMessageIds = ConcurrentHashMap.newKeySet<Long>()
    
    fun register() {
        jda.listener<MessageReceivedEvent> { e ->
            val serverId = e.guild.idLong
            val playerId = e.author.idLong
            val channel = e.channel
            val message = e.message
            val content = message.contentRaw
            
            if (message.author.idLong == jda.selfUser.idLong) {
                ignoreMessageIds.add(message.idLong)
                return@listener
            }
            
            if (message.author.isBot) return@listener
            
            val server = ServerService.getServer(serverId) ?: return@listener
            val serverChannelId = server.channelId ?: return@listener
            
            if (serverChannelId != e.channel.idLong) return@listener
            
            if (server.lastSender == playerId) {
                val botMessage = channel.sendMessage(
                    SenderRepeatMessage.message
                ).await()
                
                ignoreMessageIds.add(message.idLong)
                
                botMessage.delete().queueAfter(5, TimeUnit.SECONDS)
                message.delete().queueAfter(5, TimeUnit.SECONDS)
                
                return@listener
            }
            
            handle(
                serverId,
                playerId,
                content,
                server,
                message,
                channel
            )
        }
        
        jda.listener<MessageDeleteEvent> { e ->
            if (ignoreMessageIds.remove(e.messageIdLong)) return@listener
            
            val serverId = e.guild.idLong
            val score = ServerService.getCurrentScore(serverId) ?: return@listener
            
            val channel = e.channel
            if (ServerService.getChannel(serverId) != channel.idLong) return@listener
            
            channel.sendMessage(
                MessageDeleteMessage.getMessage(score)
            ).queue()
        }
        
        jda.listener<MessageUpdateEvent> { e ->
            val serverId = e.guild.idLong
            val score = ServerService.getCurrentScore(serverId) ?: return@listener
            
            val channel = e.channel
            if (ServerService.getChannel(serverId) != channel.idLong) return@listener
            
            channel.sendMessage(
                MessageUpdateMessage.getMessage(score)
            ).queue()
        }
    }
    
    @Synchronized
    private fun handle(
        serverId: Long,
        playerId: Long,
        content: String,
        server: ServerModel,
        message: Message,
        channel: MessageChannelUnion
    ) {
        try {
            val strInt = content.toUIntOrNull()
            val strMath = ExpressionBuilder(content).build().evaluate().toUInt()
            
            val isMath = strInt != strMath
            val result = if (isMath) strMath else strInt
            
            val correect = server.currentScore + 1u
            
            if (result == correect) {
                message.addReaction(Emoji.fromFormatted("\uD83E\uDDEE")).queue()
                
                runBlocking {
                    ServerService.incrementScore(serverId)
                    ServerService.updateSender(serverId, playerId)
                    
                    PlayerService.incrementCorrect(playerId, serverId)
                    if (isMath) PlayerService.incrementMath(playerId, serverId)
                }
            } else {
                message.addReaction(Emoji.fromFormatted("\uD83D\uDCA5")).queue()
                
                channel.sendMessage(
                    WrongNumberMessage.getMessage(result, correect)
                ).queue()
                
                runBlocking {
                    ServerService.resetScore(serverId)
                    PlayerService.incrementWrong(playerId, serverId)
                }
            }
        } catch (_: IllegalArgumentException) {
            channel.sendMessage(
                NonArithmeticExceptionMessage.message
            ).queue { botMessage ->
                botMessage.delete().queueAfter(5, TimeUnit.SECONDS)
            }
            
            ignoreMessageIds.add(message.idLong)
            
            message.delete().queueAfter(5, TimeUnit.SECONDS)
        } catch (_: ArithmeticException) {
            channel.sendMessage(
                ArithmeticExceptionMessage.message
            ).queue { botMessage ->
                botMessage.delete().queueAfter(5, TimeUnit.SECONDS)
            }
            
            ignoreMessageIds.add(message.idLong)
            
            message.delete().queueAfter(5, TimeUnit.SECONDS)
        }
    }
}