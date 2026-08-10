package ru.ynovka.messages

import dev.minn.jda.ktx.messages.MessageCreate
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import ru.ynovka.database.service.ServerService

object CurrentNumberMessage {
    
    suspend fun getMessage(
        serverId: Long?
    ): MessageCreateData {
        if (serverId == null) {
            return MessageCreate(useComponentsV2 = true) {
                container {
                    text("Эм, я могу подсказать число только на сервере.")
                }
            }
        }
        
        val currentScore = ServerService.getCurrentScore(serverId) ?: return MessageCreate(useComponentsV2 = true) {
            container {
                text("Кажется числа нету? Странно как-то...")
            }
        }
        
        return MessageCreate(useComponentsV2 = true) {
            container {
                text("Помнится последним было **$currentScore**?")
            }
        }
    }
    
}