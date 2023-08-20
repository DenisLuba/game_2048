package com.example.game_2024.model

import android.content.Context
import androidx.room.Room
import com.example.game_2024.model.dataBaseRoom.AppDao
import com.example.game_2024.model.dataBaseRoom.AppDataBase
import com.example.game_2024.model.dataBaseRoom.RoomDBRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Repository private constructor(applicationContext: Context) {

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(_applicationContext: Context): Repository {

            if (instance != null) {
                synchronized(this) {
                    return if (instance != null) instance!!
                    else Repository(_applicationContext)
                }
            } else synchronized(this) {
                return Repository(_applicationContext)
            }
        }
    }

    lateinit var model: Model

    private val appDatabase: AppDataBase by lazy {
        Room.databaseBuilder(applicationContext, AppDataBase::class.java, "database.db")
            .createFromAsset("room_article.db")
            .build()
    }
    private val dao: AppDao by lazy {
        appDatabase.getAppDao()
    }
    private val roomDBRepository: RoomDBRepository by lazy {
        RoomDBRepository(dao, Dispatchers.IO)
    }
    private suspend fun setModel(height: Int, with: Int, id: Int) {
        model = roomDBRepository.getModelById(id) ?: Model.getInstance(height, with, id).apply {
            saveField(this)
        }
    }

    suspend fun saveField(model: Model) {
        roomDBRepository.saveGameField(model)
    }

    fun getModel(height: Int, with: Int, id: Int): Model = runBlocking<Model> {
        setModel(height, with, id)
        model
    }

}





