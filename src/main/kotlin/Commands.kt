package ru.ynovka

import dev.minn.jda.ktx.events.onCommand
import net.dv8tion.jda.api.interactions.commands.build.Commands.slash
import ru.ynovka.Main.Companion.jda
import ru.ynovka.messages.Settings

object Commands {
    
    fun register() {
        jda.updateCommands()
            .addCommands(
                slash("настройки", "настройка бота")
            )
            .queue()
        
        jda.onCommand("настройки") { e ->
            e.replyModal(
                Settings.modal
            ).queue()
        }
    }
    
}