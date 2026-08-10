package ru.ynovka.messages

import dev.minn.jda.ktx.messages.MessageCreate
import dev.minn.jda.ktx.messages.named
import net.dv8tion.jda.api.components.separator.Separator

object SettingsMessage {
    
    private val img = javaClass.getResourceAsStream("/smart.jpg")!!.named("smart.jpg")
    val message = MessageCreate(useComponentsV2 = true) {
        container {
            mediaGallery {
                item(img)
            }
            
            separator(isDivider = true, spacing = Separator.Spacing.LARGE)
            
            text("""
                # Йоу, всем привет!!!!!
                Я Умный человек в очках,
                можете называть меня **СЧЕТОВОД**
                Отныне этот чат принадлежит **МНЕ**
                
                ## Новые правила чата:
                - МОЖНО ТОЛЬКО **СЧИТАТЬ**
                - ОШИБКА - И **ВСЁ СНАЧАЛА**
                - СЧИТАЕМ **ПО ОЧЕРЕДИ**
            """.trimIndent())
        }
    }
    
}