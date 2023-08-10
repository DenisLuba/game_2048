package com.example.game_2024.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.game_2024.model.Model

typealias move = () -> Unit

class ViewModel2024(application: Application, dimensions: IntArray) :
    AndroidViewModel(application) {

//    Variables

    private var score = 0
    private var maxTile = 0
    private var isWinner = false
    private var noMovies = false

    private val model = Model.getInstance(dimensions.component1(), dimensions.component2())

//    **********************************************************************************************

//    LIVEDATA

    val liveDataField = MutableLiveData<List<MutableList<Int>>>().apply {
        value = model.gameField
    }

    val liveDataWinner = MutableLiveData<Boolean>()
    val liveDataLost = MutableLiveData<Boolean>()
    val liveDataScore = MutableLiveData<Int>().apply { value = 0 }

//    **********************************************************************************************

//    Public functions

    fun resetGame() = action { model.resetGameTiles() }

    fun left() = action { model.left() }

    fun right() = action { model.right() }

    fun up() = action { model.up() }

    fun down() = action { model.down() }

//    **********************************************************************************************

//    Additional function

    private fun action(direction: move) {
        direction.invoke()
        liveDataField.value = model.gameField
        liveDataScore.value = model.score
        maxTile = model.maxTile
        if (maxTile == WINNING_TILE) liveDataWinner.value = true
        if (!model.canMove()) liveDataLost.value = true
    }

    companion object {
        private const val WINNING_TILE = 2048
    }
}
