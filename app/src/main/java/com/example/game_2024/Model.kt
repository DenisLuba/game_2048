package com.example.game_2024

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Model: ViewModel() {

    val liveData = MutableLiveData<Int>() // may be not Int

    private val gameTiles: Array<Array<Tile>> by lazy {
        Array(FIELD_HEIGHT) { Array(FIELD_WIDTH) { Tile() } }
    }
    companion object {
        private const val FIELD_WIDTH = 4
        private const val FIELD_HEIGHT = 4
    }
}