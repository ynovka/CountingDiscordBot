package ru.ynovka.messages

import dev.minn.jda.ktx.interactions.components.Thumbnail
import dev.minn.jda.ktx.messages.MessageCreate
import dev.minn.jda.ktx.messages.named

object SenderRepeatMessage {
    
    private val img = javaClass.getResourceAsStream("/smart_nonono.jpeg")!!.named("smart_nonono.jpeg")
    val message = MessageCreate(useComponentsV2 = true) {
        container {
            section(
                accessory = Thumbnail(img)
            ) {
                text("""
                    Стоп-стоп-стоп!
                    Мы тут считаем по очереди!
                    Дай возможность другим
                """.trimIndent())
            }
        }
    }
    
}