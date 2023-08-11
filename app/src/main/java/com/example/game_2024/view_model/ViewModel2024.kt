package com.example.game_2024.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.game_2024.model.Model

typealias move = () -> Unit

class ViewModel2024(application: Application, dimensions: IntArray) :
    AndroidViewModel(application) {

    private val model = Model.getInstance(dimensions.component1(), dimensions.component2())

//    **********************************************************************************************

//    LIVEDATA

    val liveDataField = MutableLiveData<List<MutableList<Int>>>().apply {
        value = model.gameField
    }

    val liveDataWinner = MutableLiveData<Boolean>().apply { value = model.maxTile == WINNING_TILE }
    val liveDataLost = MutableLiveData<Boolean>().apply { value = !model.canMove() }
    val liveDataScore = MutableLiveData<Int>().apply { value = model.score }

//    **********************************************************************************************

//    Public functions

    fun resetGame() = action { model.resetGameTiles() }

    fun rollback() = action { model.rollback() }

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
        liveDataWinner.value = model.maxTile == WINNING_TILE
        liveDataLost.value = !model.canMove()
    }

    companion object {
        private const val WINNING_TILE = 2048
    }
}
