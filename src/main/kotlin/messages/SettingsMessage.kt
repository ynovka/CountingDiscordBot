package ru.ynovka.messages

import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.buttons.ButtonStyle
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.thumbnail.Thumbnail
import dev.minn.jda.ktx.interactions.components.button
import dev.minn.jda.ktx.messages.MessageCreate
import net.dv8tion.jda.api.components.section.Section
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import ru.ynovka.Main.Companion.jda

object SettingsMessage {
    
    val container = Container.of(
        Section.of(
            Thumbnail.fromUrl("https://cdn.discordapp.com/attachments/1300559227315818516/1536089218298945596/images_1.jpg?ex=6a7a2210&is=6a78d090&hm=c4f6909c56a0dcb50064e14572f542142b571de83ea6ca6c647a4b17ef9ab375&"),
            
            TextDisplay.of("## ЭТОТ ЧАТ ЗАХВАЧЕН :rotating_light: !!! "),
            TextDisplay.of("\n Некий умный человек в очках звать которого ${jda.selfUser.name} запретил вам буквы!"),
            TextDisplay.of("\nОтныне в этом чате вам разрешено только считать!!"),
            TextDisplay.of("-# фух... кажется это не так сложно..."),
        ),
        Separator.createDivider(Separator.Spacing.LARGE),
    )
    
    val message = MessageCreateBuilder()
        .useComponentsV2()
        .setComponents(container)
        .build()
}