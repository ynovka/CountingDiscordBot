package ru.ynovka.database.service

import ru.ynovka.database.repository.PlayerRepository
import ru.ynovka.database.repository.ServerRepository

object ServerService {
    
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
    }
    
}