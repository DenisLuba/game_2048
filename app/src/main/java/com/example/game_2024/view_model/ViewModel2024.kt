package com.example.game_2024.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.game_2024.model.Model
import com.example.game_2024.model.Repository
import kotlinx.coroutines.launch

typealias move = () -> Unit

class ViewModel2024(application: Application, args: IntArray) :
    AndroidViewModel(application) {

    private val repository =
        Repository.getInstance(application.applicationContext, args.component3())
    private val model: Model = repository.getModel(args.component1(), args.component2())

//    **********************************************************************************************

//    LIVEDATA

    val liveDataField = MutableLiveData<List<MutableList<Int>>>().apply { value = model.gameField }
    val liveDataWinner = MutableLiveData<Boolean>().apply { value = model.maxTile == WINNING_TILE }
    val liveDataLost = MutableLiveData<Boolean>().apply { value = !model.canMove() }
    val liveDataScore = MutableLiveData<Int>().apply { value = model.score }
    val liveDataMaxScore = MutableLiveData<Int>().apply { value = model.maxScore }

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
        viewModelScope.launch { direction.invoke() }
        viewModelScope.launch { repository.saveModel(model) }
        viewModelScope.launch { liveDataField.value = model.gameField }
        viewModelScope.launch { liveDataScore.value = model.score }
        viewModelScope.launch { liveDataMaxScore.value = model.maxScore }
        viewModelScope.launch { liveDataWinner.value = model.maxTile == WINNING_TILE }
        viewModelScope.launch { liveDataLost.value = !model.canMove() }
    }

    companion object { private const val WINNING_TILE = 2048 }
}
