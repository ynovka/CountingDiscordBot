package ru.ynovka

import dev.minn.jda.ktx.jdabuilder.light
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy

class Main {
    companion object {
        lateinit var jda: JDA
            private set
        
        @JvmStatic
        fun main(args: Array<String>) {
            jda = light("") {
                enableIntents(
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.MESSAGE_CONTENT,
                )
                
                setMemberCachePolicy(MemberCachePolicy.ALL)
                setChunkingFilter(ChunkingFilter.ALL)
            }
        }
    }
}