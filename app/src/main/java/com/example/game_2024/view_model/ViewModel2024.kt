package com.example.game_2024.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.game_2024.model.Model

class ViewModel2024(application: Application, dimensions: IntArray): AndroidViewModel(application) {

    val liveData = MutableLiveData<Int>()

    private val model = Model(dimensions.component1(), dimensions.component2())

    fun getGameField(): List<List<Int>> = model.gameField

    fun getScore(): Int = model.score

    fun getMaxTile(): Int = model.maxTile

    fun resetGame(): Unit = model.resetGameTiles()
}