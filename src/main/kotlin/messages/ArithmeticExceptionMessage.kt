package ru.ynovka.messages

import dev.minn.jda.ktx.interactions.components.Thumbnail
import dev.minn.jda.ktx.messages.MessageCreate
import dev.minn.jda.ktx.messages.named

object ArithmeticExceptionMessage {
    
    private val img = javaClass.getResourceAsStream("/smart_boom.jpeg")!!.named("smart_boom.jpeg")
    val message = MessageCreate(useComponentsV2 = true) {
        container {
            
            section(
                accessory = Thumbnail(img)
            ) {
                text("""
                    Ой, ой, ой!
                    Такое я посчитать не могу!
                    Попробуй ещё раз
                """.trimIndent())
            }
        }
    }
    
}