package ru.ynovka.messages

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.interactions.components.Thumbnail
import dev.minn.jda.ktx.messages.MessageCreate
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import ru.ynovka.Main.Companion.jda
import ru.ynovka.database.model.PlayerStat
import ru.ynovka.database.service.PlayerService

object TopPlayersMessage {
    
    private data class Player(
        val n: Int,
        val username: String,
        val avatar: String,
        val correct: UInt,
        val wrong: UInt,
        val math: UInt,
    )
    
    suspend fun getTop(
        serverId: Long,
        playerId: Long,
        stat: PlayerStat
    ): MessageCreateData {
        println("11111")
        val top = PlayerService.getTop(serverId, playerId, stat)
            .map {
                val user = jda.retrieveUserById(it.value.playerId).await()
                Player(
                    it.key,
                    user.name,
                    user.effectiveAvatarUrl,
                    it.value.correct,
                    it.value.wrong,
                    it.value.math
                )
            }
        
        if (top.isEmpty()) {
            return MessageCreate(useComponentsV2 = true) {
                container {
                    text("Таблица лидеров пуста...")
                }
            }
        }
        
        return MessageCreate(useComponentsV2 = true) {
            top.forEach { player ->
                container {
                    section(
                        accessory = Thumbnail(player.avatar)
                    ) {
                        text("${player.n}. ${player.username}")
                    }
                }
            }
        }
    }
  
}