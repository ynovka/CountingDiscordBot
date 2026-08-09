package ru.ynovka.database

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import ru.ynovka.database.DataBase.Companion.mariadb
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object DatabaseExecutor {
    private val executor: ExecutorService = Executors.newVirtualThreadPerTaskExecutor()
    private val dispatcher = executor.asCoroutineDispatcher()
    
    suspend fun <T> transaction(block: Transaction.() -> T): T =
        withContext(dispatcher) {
            transaction(mariadb) {
                block()
            }
        }
    
    fun shutdown() {
        dispatcher.close()
    }
}