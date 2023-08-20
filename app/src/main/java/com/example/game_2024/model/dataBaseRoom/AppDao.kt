package com.example.game_2024.model.dataBaseRoom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AppDao {

    @Query("SELECT * FROM fields WHERE id = :id")
    suspend fun getDBGameFieldEntity(id: Int): DBGameFieldEntity?

    @Update
    suspend fun updateDBGameFieldEntity(dbGameFieldEntity: DBGameFieldEntity)

    @Insert
    suspend fun createDBGameFieldEntity(dbGameFieldEntity: DBGameFieldEntity)
}