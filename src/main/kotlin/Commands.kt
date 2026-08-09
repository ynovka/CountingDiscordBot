package ru.ynovka

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.Commands.slash
import ru.ynovka.Main.Companion.jda

object Commands {
    
    fun register() {
        jda.updateCommands()
            .addCommands(
                slash("настройки", "настройка бота")
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
            )
            .queue()
    }
    
}