package ru.ynovka.modals

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
        
        jda.onCommand("настройки") { e ->
            e.replyModal(modal).queue()
        }
        
        jda.listener<ModalInteractionEvent> { e ->
            if (e.modalId != modal.id) return@listener
            
            val server = e.guild?.idLong ?: return@listener
            
            val shouldRemoveDataString = e.getValue("remove-data")
                ?.getAsStringList()
                ?: false
            if (shouldRemoveDataString != "0") {
                PlayerService.deleteAllWithServer(server)
            }
            
            val channelId = e.getValue("channel-select")
                ?.getAsStringList()
                ?.first()
                ?.toLong()
                ?: return@listener
            
            if (ServerService.getChannel(server) == channelId) return@listener
            
            val channel = jda.getTextChannelById(channelId) ?: return@listener
            
            ServerService.setChannel(server, channelId)
            
            channel.retrievePinnedMessages().queue { messages ->
                messages.forEach { message ->
                    channel.unpinMessageById(message.message.id)
                }
            }
            
            channel.sendMessage(
                SettingsMessage.message
            ).queue {
                channel.pinMessageById(it.id)
            }
        }
        
    }
    
    private val noOption = SelectOption(
        "НЕТ",
        "0",
        "",
        Emoji.fromUnicode("🔴")
    )
    private val yesOption = SelectOption(
        "ДА",
        "1",
        "ДЕЙСТВИЕ НЕЛЬЗЯ БУДЕТ ОТМЕНИТЬ!!!",
        Emoji.fromUnicode("🟢")
    )
    
    val modal = Modal.create("settings_modal", "Настройка бота")
        .addComponents(
            Label.of(
                "Канал для счёта",
                EntitySelectMenu.create("channel-select", EntitySelectMenu.SelectTarget.CHANNEL).build()
            ),
            Label.of(
                "Очистить таблицу лидеров?",
                StringSelectMenu.create("remove-data")
                    .addOptions(listOf(noOption, yesOption))
                    .setDefaultOptions(noOption)
                    .build()
            )
        )
        .build()
}