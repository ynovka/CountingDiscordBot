package ru.ynovka.messages

import dev.minn.jda.ktx.interactions.components.Thumbnail
import dev.minn.jda.ktx.messages.MessageCreate
import dev.minn.jda.ktx.messages.named

object WrongNumberMessage {
    
    private val img = javaClass.getResourceAsStream("/smart_nonono.jpeg")!!.named("smart_nonono.jpeg")
    fun getMessage(typed: UInt, correct: UInt, best: UInt) = MessageCreate(useComponentsV2 = true) {
        container {
            section(
                accessory = Thumbnail(img)
            ) {
                text("""
                    А вот и не правильно!
                    Ты посчитал что это **$typed**,
                    Нооо это **$correct**!
                    
                    Начинайте считать сначала!
                """.trimIndent())
            }
            text("-# Рекорд: $best")
        }
    }
    
}