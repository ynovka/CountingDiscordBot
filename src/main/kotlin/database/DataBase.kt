package ru.ynovka.database

import io.github.cdimascio.dotenv.Dotenv
import org.jetbrains.exposed.v1.jdbc.Database

class DataBase {
    companion object {
        lateinit var database: Database
            private set
    }
    
    private val ip = Dotenv.load().get("DB_IP").toString()
    private val port = Dotenv.load().get("DB_PORT").toString()
    private val table = Dotenv.load().get("DB_TABLE").toString()
    private val user = Dotenv.load().get("DB_USER").toString()
    private val password = Dotenv.load().get("DB_PASSWORD").toString()
    
    init {
        database = Database.connect(
            "jdbc:mariadb://$ip:$port/$table",
            driver = "org.mariadb.jdbc.Driver",
            user = user,
            password = password
        )
    }
}