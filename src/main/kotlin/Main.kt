package ru.ynovka

import io.github.cdimascio.dotenv.Dotenv
import dev.minn.jda.ktx.jdabuilder.light
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import ru.ynovka.database.DataBase

class Main {
    companion object {
        lateinit var jda: JDA
            private set
        
        private val TOKEN = Dotenv.load().get("TOKEN").toString()
        
        @JvmStatic
        fun main(args: Array<String>) {
            jda = light(TOKEN) {
                enableIntents(
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.MESSAGE_CONTENT,
                )
                
                setMemberCachePolicy(MemberCachePolicy.ALL)
                setChunkingFilter(ChunkingFilter.ALL)
            }
            
            DataBase()
            
            GuildEvents.register()
        }
    }
}