package com.example.game_2024.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.game_2024.model.Model
import com.example.game_2024.model.Repository
import kotlinx.coroutines.launch

typealias move = () -> Unit

class ViewModel2024(application: Application, private val args: IntArray) :
    AndroidViewModel(application) {

    private val id get() = args[2] * (args[1] - 1) + args[0]


    //    private val model = Model.getInstance(args.component1(), args.component2(), id)
    private val repository: Repository = Repository.getInstance(application.applicationContext)
    private var model = repository.getModel(args.component1(), args.component2(), id)

//    **********************************************************************************************

//    LIVEDATA


    val liveDataField = MutableLiveData<List<MutableList<Int>>>().apply {
        value = model.gameField
    }

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
        direction.invoke()
        saveFieldStateInDB()
        liveDataField.value = model.gameField
        liveDataScore.value = model.score
        liveDataMaxScore.value = model.maxScore
        liveDataWinner.value = model.maxTile == WINNING_TILE
        liveDataLost.value = !model.canMove()
    }

    private fun saveFieldStateInDB() {
        viewModelScope.launch {
            repository.saveField(model)
        }
    }

    private fun setModel() {
        viewModelScope.launch {
            model = repository.getModel(args.component1(), args.component2(), id)
        }
    }

    companion object {
        private const val WINNING_TILE = 2048
    }
}
