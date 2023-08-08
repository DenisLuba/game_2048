package com.example.game_2024.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.game_2024.model.Model

class ViewModel2024(application: Application, fieldSize: Int): AndroidViewModel(application) {
    //class ViewModel2024: ViewModel() {

    val liveData = MutableLiveData<Int>() // may be not Int

    private val model = Model(fieldSize)

    fun getGameField(): List<List<Int>> = model.gameField

    fun getScore(): Int = model.score

    fun getMaxTile(): Int = model.maxTile
}