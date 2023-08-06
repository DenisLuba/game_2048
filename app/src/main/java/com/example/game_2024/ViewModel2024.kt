package com.example.game_2024

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ViewModel2024(application: Application, dimensions: IntArray): AndroidViewModel(application) {
    //class ViewModel2024: ViewModel() {

    val liveData = MutableLiveData<Int>() // may be not Int

    private val model = Model(dimensions[0], dimensions[1])

    private fun getGameField(): List<MutableList<Tile>> = model.gameField

    private fun getScore(): Int = model.score
}