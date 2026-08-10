package ru.ynovka.modals

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.components.SelectOption
import net.dv8tion.jda.api.components.label.Label
import net.dv8tion.jda.api.components.selections.EntitySelectMenu
import net.dv8tion.jda.api.components.selections.StringSelectMenu
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.modals.Modal
import ru.ynovka.Main.Companion.jda
import ru.ynovka.database.service.PlayerService
import ru.ynovka.database.service.ServerService
import ru.ynovka.messages.SettingsMessage

object SettingsModal {
    
    fun register() {
        
        jda.listener<ModalInteractionEvent> { e ->
            if (e.modalId != "settings_modal") return@listener
            
            e.deferReply(true).queue()
            
            val server = e.guild?.idLong ?: return@listener
            
            var hasChanges = false
            
            val removeData = e.getValue("remove_data")
                ?.getAsStringList()
                ?.firstOrNull()
                ?: "no"
            
            if (removeData == "yes") {
                hasChanges = true
                
                ServerService.resetScore(server)
                PlayerService.deleteAllWithServer(server)
            }
            
            val newChannelId = e.getValue("channel_select")
                ?.getAsStringList()
                ?.firstOrNull()
                ?.toLongOrNull()
                ?: return@listener
            
            val oldChannelId = ServerService.getChannel(server)
            
            if (oldChannelId != newChannelId) {
                hasChanges = true
                
                val channel = jda.getTextChannelById(newChannelId)
                
                if (channel == null) {
                    e.hook.editOriginal("ошибка!").queue()
                    return@listener
                }
                
                ServerService.setChannel(server, newChannelId)
                
                channel.retrievePinnedMessages().await()
                    .forEach {
                        it.message.unpin().await()
                    }
                
                channel.sendMessage(SettingsMessage.message)
                    .await()
                    .pin()
                    .await()
            }
            
            e.hook.editOriginal(
                if (hasChanges) "готово!"
                else "ничего не изменилось!"
            ).queue()
        }
        
    }
    
    private val noOption = SelectOption(
        "НЕТ",
        "no",
        "",
        Emoji.fromUnicode("🔴")
    )
    private val yesOption = SelectOption(
        "ДА",
        "yes",
        "ДЕЙСТВИЕ НЕЛЬЗЯ БУДЕТ ОТМЕНИТЬ!!!",
        Emoji.fromUnicode("🟢")
    )
    
    suspend fun getModal(server: Long?): Modal {
        val channelSelect = EntitySelectMenu.create(
            "channel_select",
            EntitySelectMenu.SelectTarget.CHANNEL
        )
        
        server?.let {
            val channelId = ServerService.getChannel(it) ?: return@let
            channelSelect.setDefaultValues(EntitySelectMenu.DefaultValue.channel(channelId))
        }
        
        return Modal.create("settings_modal", "Настройка бота")
            .addComponents(
                Label.of(
                    "Канал для счёта",
                    channelSelect.build()
                ),
                Label.of(
                    "Очистить таблицу лидеров?",
                    StringSelectMenu.create("remove_data")
                        .addOptions(listOf(noOption, yesOption))
                        .setDefaultOptions(noOption)
                        .build()
                )
            )
            .build()
    }
}