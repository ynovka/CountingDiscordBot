package ru.ynovka.database.service

import ru.ynovka.database.repository.PlayerRepository
import ru.ynovka.database.repository.ServerRepository
import java.util.concurrent.ConcurrentHashMap

typealias ChannelId = Long
typealias ServerId = Long

object ServerService {
    
    private val channelsCache = ConcurrentHashMap<ServerId, ChannelId>()
    
    suspend fun updateServers(servers: Set<Long>) {
        val registered = ServerRepository.getServers()
        
        val toAdd = servers.minus(registered)
        val toRemove = registered.minus(servers)
        
        toAdd.forEach { addServer(it) }
        toRemove.forEach { removeServer(it) }
    }
    
    suspend fun addServer(server: Long) = ServerRepository.addServer(server)
    
    suspend fun removeServer(server: Long) {
        ServerRepository.removeServer(server)
        PlayerRepository.deleteAllWithServer(server)
        channelsCache.remove(server)
    }
    
    suspend fun getChannel(server: Long): Long? {
        channelsCache[server]?.let { return it }
        
        val channel = ServerRepository.getChannel(server)
        
        channel?.let { channelsCache[server] = it }
        
        return channel
    }
    
    suspend fun setChannel(server: Long, newChannel: Long) {
        ServerRepository.setChannel(server, newChannel)
        channelsCache[server] = newChannel
    }
    
    suspend fun resetScore(server: Long) = ServerRepository.resetScore(server)
    
}