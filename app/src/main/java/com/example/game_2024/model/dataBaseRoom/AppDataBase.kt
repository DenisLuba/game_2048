package com.example.game_2024.model.dataBaseRoom

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson

@Database(
    version = 1,
    entities = [
        DBGameFieldEntity::class
    ]
)
@TypeConverters(Converter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getAppDao(): AppDao
}

