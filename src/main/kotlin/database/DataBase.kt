package ru.ynovka.database

import org.jetbrains.exposed.v1.jdbc.Database

class DataBase {
    companion object {
        lateinit var database: Database
            private set
    }
    
    init {
        database = Database.connect(
            "jdbc:mariadb://45.90.247.243:3306/myshore",
            driver = "org.mariadb.jdbc.Driver",
            user = "myshore",
            password = "5d0fdeef723351e423290c29b3946eb6f421a59881a45ca7bbf3d86ff981877c"
        )
    }
}