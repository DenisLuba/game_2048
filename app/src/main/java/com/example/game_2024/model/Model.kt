package com.example.game_2024.model

import kotlin.math.max
import kotlin.random.Random

class Model(fieldSize: Int = 4) {

    var score: Int = 0
    var maxTile: Int = 0
    private var isSaveNeeded = true

    val gameField: List<MutableList<Int>> = List(fieldSize) { MutableList(fieldSize) { 0 } }
    private val flippedGameField: List<MutableList<Int>> = List(fieldSize) { MutableList(fieldSize) { 0 } }

    init {
        resetGameTiles()
    }

    private fun resetGameTiles() {
        gameField.forEach { list -> list.map { 0 } }
        repeat(2) { addRandomTile() }
    }

    private fun addRandomTile() {
        val tiles: MutableList<IntArray?> = getEmptyTiles().toMutableList()
        if (tiles.isNotEmpty()) {
            val randomTile = Random.nextInt(tiles.size)
            val row = tiles[randomTile]?.component1() ?: -1
            val column = tiles[randomTile]?.component2() ?: -1
            val num = if (Random.nextInt(10) < 9) 2 else 4
            gameField[row][column] = num
        }
    }

//    private fun getEmptyTiles(): List<Int> = gameField.flatten().filter { it == 0 }

    private fun getEmptyTiles(): List<IntArray?> = gameField.mapIndexed { row, list -> list.mapIndexed { column, num -> if (num == 0) intArrayOf(row, column) else null } }.flatten().filterNotNull()

//    Moving

    fun left() {
        if (isSaveNeeded) saveState(gameField)
        var isChanged = false
        var wasCompressed: Boolean
        var wasMerged: Boolean

        for (tiles in gameField) {
            wasCompressed = compressTiles(tiles)
            wasMerged = mergeTiles(tiles)
            if (wasCompressed || wasMerged) isChanged = true
        }

        if (isChanged) addRandomTile()
        isSaveNeeded = true
    }

    fun right() {
        saveState(gameField)
        flipField()
        flipField()
        left()
        flipField()
        flipField()
    }

    fun up() {
        flipField()
        flipField()
        flipField()
        left()
        flipField()
    }

    fun down() {
        flipField()
        left()
        flipField()
        flipField()
        flipField()
    }


//    Extra methods for moving

    private fun compressTiles(tiles: MutableList<Int>): Boolean =
        tiles.filter { it != 0 }
            .plus(tiles.filter { it == 0 })
            .let {
                val isChanged = it != tiles
                for (i in tiles.indices) tiles[i] = it[i]
                isChanged
            }

    private fun mergeTiles(tiles: MutableList<Int>): Boolean {
        var isChanged = false

        for (i in tiles.indices) {
            if ((i + 1) < tiles.size && tiles[i] == tiles[i + 1] && tiles[i] != 0) {
                tiles[i] *= 2
                tiles[i + 1] = 0
                maxTile = max(tiles[i], maxTile)
                score += tiles[i]
                isChanged = true
                compressTiles(tiles)
            }
        }
        return isChanged
    }

    private fun flipField() {
        for (i in gameField.indices)
            for (j in gameField[i].indices)
                flippedGameField[j][gameField.lastIndex - i] = gameField[i][j]

        for (i in flippedGameField.indices)
            for (j in flippedGameField[i].indices)
                gameField[i][j] = flippedGameField[i][j]
    }

    fun canMove(): Boolean {
        for (i in gameField.indices) {
            for (j in gameField[i].indices) {
                if (
                    gameField[i][j] == 0
                    || (i > 0 && gameField[i][j] == gameField[i - 1][j])
                    || (j > 0 && gameField[i][j] == gameField[i][j - 1])
                ) return true
            }
        }
        return false
    }


//    Save state

    private fun saveState(tiles: List<MutableList<Int>>) {

    }
}

//fun main() {
//    val model = Model(4)
//    model.gameField = listOf(
//        mutableListOf(0, 0, 2, 2),
//        mutableListOf(4, 2, 4, 2),
//        mutableListOf(0, 2, 2, 4),
//        mutableListOf(4, 2, 2, 4)
//    )
//
//    model.gameField.forEach(::println)
//    println()
//
//    model.right()
//
//    model.gameField.forEach(::println)
//    println(model.canMove())
//
//}


