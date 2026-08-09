package ru.ynovka.messages

import dev.minn.jda.ktx.messages.MessageCreate


object SettingsMessage {
    val message = MessageCreate(useComponentsV2 = true) {
        container {
            // Всем привет, я
        }
    }
}