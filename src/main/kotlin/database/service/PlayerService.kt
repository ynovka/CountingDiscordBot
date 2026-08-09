package ru.ynovka.database.service

import ru.ynovka.database.repository.PlayerRepository

object PlayerService {
    
    suspend fun deleteAllWithServer(server: Long) = PlayerRepository.deleteAllWithServer(server)
    
}