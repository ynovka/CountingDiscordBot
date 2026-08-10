package ru.ynovka.database.service

import ru.ynovka.database.model.ServerModel
import ru.ynovka.database.repository.ServerRepository
import java.util.concurrent.ConcurrentHashMap

typealias ServerId = Long

object ServerService {
    
    private val serversCache = ConcurrentHashMap<ServerId, ServerModel>()
    
    suspend fun updateServers(servers: Set<Long>) {
        val registered = ServerRepository.getServers()
        
        val toAdd = servers.minus(registered)
        val toRemove = registered.minus(servers)
        
        toAdd.forEach { addServer(it) }
        toRemove.forEach { removeServer(it) }
    }
    
    suspend fun addServer(server: Long) = ServerRepository.addServer(server)
    
    suspend fun getServer(server: Long) = ServerRepository.getServer(server)
    
    suspend fun removeServer(server: Long) {
        ServerRepository.removeServer(server)
        serversCache.remove(server)
    }
    
    suspend fun getChannel(serverId: Long): Long? {
        serversCache[serverId]?.let { return it.channelId }
        
        val server = ServerRepository.getServer(serverId) ?: return null
        serversCache[serverId] = server
        
        return server.channelId
    }
    
    suspend fun setChannel(serverId: ServerId, newChannel: Long) {
        ServerRepository.setChannel(serverId, newChannel)
        
        serversCache.computeIfPresent(serverId) { _, model ->
            model.copy(channelId = newChannel)
        }
    }
    
    suspend fun updateSender(serverId: ServerId, sender: Long) {
        ServerRepository.updateSender(serverId, sender)
        
        serversCache.computeIfPresent(serverId) { _, model ->
            model.copy(lastSender = sender)
        }
    }
    
    suspend fun getCurrentScore(serverId: Long): UInt? {
        serversCache[serverId]?.let { return it.currentScore }
        
        val server = ServerRepository.getServer(serverId) ?: return null
        serversCache[serverId] = server
        
        return server.currentScore
    }
    
    suspend fun resetScore(serverId: Long) {
        ServerRepository.resetScore(serverId)
        
        serversCache.computeIfPresent(serverId) { _, model ->
            model.copy(currentScore = 0u)
        }
    }
    
    suspend fun incrementScore(server: Long) {
        ServerRepository.incrementScore(server)
        
        serversCache.computeIfPresent(server) { _, model ->
            model.copy(currentScore = model.currentScore + 1u)
        }
    }
    
}