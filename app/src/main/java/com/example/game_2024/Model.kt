package com.example.game_2024

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class Model(application: Application): AndroidViewModel(application) {
//class Model: ViewModel() {

    val liveData = MutableLiveData<Int>() // may be not Int

    private val gameTiles: List<List<Tile>> by lazy {
        List(FIELD_HEIGHT) { MutableList(FIELD_WIDTH) { Tile() } }
    }

    init {
        resetGameTiles()
    }

    private fun resetGameTiles() {
        gameTiles.forEach{ list -> list.map { it.value = 0 } }
        repeat(2) { addRandomTile() }
    }

    private fun addRandomTile() {
        val tiles: List<Tile> = getEmptyTiles()
        if (tiles.isNotEmpty()) {
            val randomTile = Random.nextInt(tiles.size)
            tiles[randomTile].value = if (Random.nextInt(10) < 9) 2 else 4
        }
    }

    private fun getEmptyTiles(): List<Tile> = gameTiles.flatten().filter { it.isEmpty() }
    companion object {
        private const val FIELD_WIDTH = 4
        private const val FIELD_HEIGHT = 4
    }
}