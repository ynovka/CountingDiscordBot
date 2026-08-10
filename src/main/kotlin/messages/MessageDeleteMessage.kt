package ru.ynovka.messages

import dev.minn.jda.ktx.interactions.components.Thumbnail
import dev.minn.jda.ktx.messages.MessageCreate
import dev.minn.jda.ktx.messages.named

object MessageDeleteMessage {
    
    private val img = javaClass.getResourceAsStream("/smart_nonono.jpeg")!!.named("smart_nonono.jpeg")
    fun getMessage(score: UInt) = MessageCreate(useComponentsV2 = true) {
        container {
            section(
                accessory = Thumbnail(img)
            ) {
                text("""
                    Кто то решил схитрить и удалил сообщение!
                    Как же хорошо что я веду счёт
                    Эм, мы остановились на **$score**
                """.trimIndent())
            }
        }
    }
    
}