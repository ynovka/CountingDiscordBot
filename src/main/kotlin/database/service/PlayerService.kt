package ru.ynovka.database.service

import ru.ynovka.database.repository.PlayerRepository

object PlayerService {
    
    suspend fun deleteAllWithServer(server: Long) = PlayerRepository.deleteAllWithServer(server)
    
    suspend fun incrementCorrect(playerId: Long, serverId: Long) =
        PlayerRepository.incrementCorrect(playerId, serverId)
    
    suspend fun incrementWrong(playerId: Long, serverId: Long) =
        PlayerRepository.incrementWrong(playerId, serverId)
    
    suspend fun incrementMath(playerId: Long, serverId: Long) =
        PlayerRepository.incrementMath(playerId, serverId)
    
}