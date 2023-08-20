package com.example.game_2024.model.dataBaseRoom

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converter {

    @TypeConverter
    fun listToJson(value: List<MutableList<Int>>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String): List<MutableList<Int>> = Gson().fromJson(value, Array<Array<Int>>::class.java).toList().map { it.toMutableList() }
}