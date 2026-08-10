package ru.ynovka.commands

import dev.minn.jda.ktx.events.onCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.Commands
import ru.ynovka.Main.Companion.jda
import ru.ynovka.database.model.PlayerStat
import ru.ynovka.messages.CurrentNumberMessage
import ru.ynovka.messages.TopPlayersMessage
import ru.ynovka.modals.SettingsModal

object Commands {
    
    fun register() {
        jda.updateCommands()
            .addCommands(
                Commands.slash("настройки", "настройка бота")
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("число", "текущее число"),
                Commands.slash("топ", "таблица лидеров")
            )
            .queue()
        
        
        jda.onCommand("настройки") { e ->
            e.replyModal(
                SettingsModal.getModal(
                    e.guild?.idLong
                )
            ).queue()
        }
        
        
        jda.onCommand("число") { e ->
            e.reply(
                CurrentNumberMessage.getMessage(
                    e.guild?.idLong
                )
            ).setEphemeral(true).queue()
        }
        
        jda.onCommand("топ") { e ->
            val serverId = e.guild?.idLong
            if (serverId == null) {
                e.reply("команда работает только на сервере!").setEphemeral(true).queue()
                return@onCommand
            }
            
            e.reply(
                TopPlayersMessage.getTop(
                    serverId,
                    e.user.idLong,
                    PlayerStat.CORRECT
                )
            ).setEphemeral(true).queue()
        }
    }
    
}