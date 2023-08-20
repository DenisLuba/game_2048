package com.example.game_2024.model.dataBaseRoom

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.example.game_2024.model.Model
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RoomDBRepository(
    private val appDao: AppDao,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getModelById(id: Int): Model? = appDao.getDBGameFieldEntity(id)?.toModel()

    private suspend fun updateGameField(model: Model) {
        withContext(ioDispatcher) {
            appDao.updateDBGameFieldEntity(DBGameFieldEntity.fromModel(model))
        }
    }

    suspend fun saveGameField(model: Model) {
        withContext(ioDispatcher) {
            if (getModelById(model.id) != null) updateGameField(model)
            val entity: DBGameFieldEntity = DBGameFieldEntity.fromModel(model)
            appDao.createDBGameFieldEntity(entity)
        }
    }
}