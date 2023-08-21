package com.example.game_2024.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class Repository private constructor(private val applicationContext: Context) {

    companion object {
        private const val PREFERENCES_FIELD = "SHARED_PREFERENCES_FIELD"
        @Volatile
        private var instance: Repository? = null

        fun getInstance(_applicationContext: Context, _maxHeight: Int): Repository {
            if (instance != null) {
                synchronized(this) {
                    return if (instance != null) instance!!
                    else Repository(_applicationContext).apply {
                        init(_maxHeight)
                    }
                }
            } else synchronized(this) {
                return Repository(_applicationContext).apply {
                    init(_maxHeight)
                }
            }
        }
    }

    private lateinit var preferences: SharedPreferences
    private var maxHeight: Int = 0

    private fun init(_maxHeight: Int) {
        maxHeight = _maxHeight
        preferences = applicationContext.getSharedPreferences(PREFERENCES_FIELD, Context.MODE_PRIVATE)
    }

    fun getModel(height: Int, width: Int): Model {
        val id = getId(height, width)
        val gsonFieldObject: String? = preferences.getString(id.toString(), null)

        val fieldObject: Field = if (gsonFieldObject != null) gsonToFieldObject(gsonFieldObject)
            else Field(
                score = 0,
                maxScore = 0,
                field = getField(height, width)
            )

        val score: Int = fieldObject.score
        val maxScore: Int = fieldObject.maxScore
        val field: List<MutableList<Int>> = fieldObject.field

        val model = Model.getInstance(score, maxScore, id, field)
        return model
    }

    fun saveModel(model: Model) {
        val score: Int = model.score
        val maxScore: Int = model.maxScore
        val field: List<MutableList<Int>> = model.gameField
        val id = model.id

        val fieldObject = Field(
            score = score,
            maxScore = maxScore,
            field = field
        )
        val gsonFieldObject: String = fieldObjectToGson(fieldObject)
        preferences.edit().putString(id.toString(), gsonFieldObject).apply()
    }

    private fun getField(height: Int, width: Int) = List(height) { MutableList(width) { 0 } }

    private fun getId(height: Int, width: Int): Int = maxHeight * (width - 1) + height

    private fun gsonToFieldObject(value: String): Field = Gson().fromJson(value, Field::class.java)

    private fun fieldObjectToGson(field: Field): String = Gson().toJson(field)

//    private val appDatabase: AppDataBase by lazy {
//        Room.databaseBuilder(applicationContext, AppDataBase::class.java, "database.db")
//            .createFromAsset("room_article.db")
//            .build()
//    }
//    private val dao: AppDao by lazy {
//        appDatabase.getAppDao()
//    }
//    private val roomDBRepository: RoomDBRepository by lazy {
//        RoomDBRepository(dao, Dispatchers.IO)
//    }
//    private suspend fun setModel(height: Int, with: Int, id: Int) {
//        model = roomDBRepository.getModelById(id) ?: Model.getInstance(height, with, id).apply {
//            saveField(this)
//        }
//    }
//
//    suspend fun saveField(model: Model) {
//        roomDBRepository.saveGameField(model)
//    }
//
//    fun getModel(height: Int, with: Int, id: Int): Model = runBlocking<Model> {
//        setModel(height, with, id)
//        model
//    }

}





