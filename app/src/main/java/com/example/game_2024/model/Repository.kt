package com.example.game_2024.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class Repository private constructor(private val applicationContext: Context) {

    companion object {
        private const val PREFERENCES_FIELD = "SHARED_PREFERENCES_FIELD"

        @Volatile
        private var instance: Repository? = null

        fun getInstance(localApplicationContext: Context, localMaxHeight: Int): Repository {
            if (instance != null) {
                synchronized(this) {
                    return if (instance != null) instance!!
                    else Repository(localApplicationContext).apply {
                        init(localMaxHeight)
                    }
                }
            } else synchronized(this) {
                return Repository(localApplicationContext).apply {
                    init(localMaxHeight)
                }
            }
        }
    }

    private lateinit var preferences: SharedPreferences
    private var maxHeight: Int = 0

    private fun init(localMaxHeight: Int) {
        maxHeight = localMaxHeight
        preferences =
            applicationContext.getSharedPreferences(PREFERENCES_FIELD, Context.MODE_PRIVATE)
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

        return Model.getInstance(score, maxScore, id, field)
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


//    **********************************************************************************************
//    Test models

//        Game over
//    fun getModel(height: Int, width: Int): Model {
//        val id = getId(height, width)
//
//        val fieldObject = Field(
//            score = 0,
//            maxScore = 0,
//            field = listOf(
//                mutableListOf(16, 32, 16, 32),
//                mutableListOf(64, 128, 64, 128),
//                mutableListOf(16, 32, 16, 32),
//                mutableListOf(64, 128, 64, 0)
//            )
//        )
//
//        val score: Int = fieldObject.score
//        val maxScore: Int = fieldObject.maxScore
//        val field: List<MutableList<Int>> = fieldObject.field
//
//        return Model.getInstance(score, maxScore, id, field)
//    }

//    Win

//    fun getModel(height: Int, width: Int): Model {
//        val id = getId(height, width)
//
//        val fieldObject = Field(
//            score = 0,
//            maxScore = 0,
//            field = listOf(
//                mutableListOf(16, 32, 16, 32),
//                mutableListOf(64, 128, 64, 128),
//                mutableListOf(16, 32, 1024, 1024),
//                mutableListOf(64, 128, 64, 0)
//            )
//        )
//
//        val score: Int = fieldObject.score
//        val maxScore: Int = fieldObject.maxScore
//        val field: List<MutableList<Int>> = fieldObject.field
//
//        return Model.getInstance(score, maxScore, id, field)
//    }
}





