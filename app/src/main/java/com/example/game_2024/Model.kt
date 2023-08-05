package com.example.game_2024

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.max
import kotlin.random.Random

class Model {

    var score: Int
    var maxTile: Int

    private val gameTiles: List<List<Tile>> by lazy {
        List(FIELD_HEIGHT) { MutableList(FIELD_WIDTH) { Tile() } }
    }

    init {
        score = 0
        maxTile = 0
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

    private fun compressTiles(tiles: MutableList<Tile>): Boolean =
        tiles.filter { !it.isEmpty() }
            .plus(tiles.filter { it.isEmpty() })
            .let {
                val isChanged = it != tiles
                for (i in tiles.indices) tiles[i] = it[i]
                isChanged
            }

    private fun mergeTiles(tiles: MutableList<Tile>): Boolean {
        var isChanged = false
        return tiles.windowed(2) {
            if (it.component1().value == it.component2().value) {
                isChanged = true
                it.component1().value *= 2
                it.component2().value = 0
                maxTile = max(it.component1().value, maxTile)
                score += it.component1().value
            }
            it.component1()
        }.let {
            compressTiles(tiles)
            isChanged
        }
    }
}