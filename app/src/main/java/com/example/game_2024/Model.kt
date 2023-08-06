package com.example.game_2024

import kotlin.math.max
import kotlin.random.Random

class Model(fieldHeight: Int = 4, fieldWidth: Int = 4) {

    var score: Int = 0
    var maxTile: Int = 0
    private var isSaveNeeded = true

    private val gameField: List<MutableList<Tile>> =
        List(fieldHeight) { MutableList(fieldWidth) { Tile() } }
    private val flippedGameField: List<MutableList<Tile>> =
        List(fieldWidth) { MutableList(fieldHeight) { Tile() } }

    init {
        resetGameTiles()
    }

    private fun resetGameTiles() {
        gameField.forEach { list -> list.map { it.value = 0 } }
        repeat(2) { addRandomTile() }
    }

    private fun addRandomTile() {
        val tiles: List<Tile> = getEmptyTiles()
        if (tiles.isNotEmpty()) {
            val randomTile = Random.nextInt(tiles.size)
            tiles[randomTile].value = if (Random.nextInt(10) < 9) 2 else 4
        }
    }

    private fun getEmptyTiles(): List<Tile> = gameField.flatten().filter { it.isEmpty() }


//    Moving

    fun left(field: List<MutableList<Tile>> = gameField) {
        if (isSaveNeeded) saveState(field)
        var isChanged = false
        var wasCompressed: Boolean
        var wasMerged: Boolean

        for (tiles in field) {
            wasCompressed = compressTiles(tiles)
            wasMerged = mergeTiles(tiles)
            if (wasCompressed || wasMerged) isChanged = true
        }

        if (isChanged) addRandomTile()
        isSaveNeeded = true
    }

    fun right() {
        saveState(gameField)
        flipField(gameField, flippedGameField)
        flipField(flippedGameField, gameField)
        left()
        flipField(gameField, flippedGameField)
        flipField(flippedGameField, gameField)
    }

    fun up() {
        flipField(gameField, flippedGameField)
        flipField(flippedGameField, gameField)
        flipField(gameField, flippedGameField)
        left(flippedGameField)
        flipField(flippedGameField, gameField)
    }

    fun down() {
        flipField(gameField, flippedGameField)
        left(flippedGameField)
        flipField(flippedGameField, gameField)
        flipField(gameField, flippedGameField)
        flipField(flippedGameField, gameField)
    }


//    Extra methods for moving

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

    private fun flipField(
        from: List<MutableList<Tile>>,
        to: List<MutableList<Tile>>
    ) {
        for (i in from.indices)
            for (j in from[i].indices)
                to[j][from.lastIndex - i].value = from[i][j].value
    }


//    Save state

    private fun saveState(tiles: List<MutableList<Tile>>) {

    }
}

//fun main() {
//    val model = Model(4, 3)
//    model.gameField = listOf(
//        mutableListOf(Tile(), Tile(2), Tile(2)),
//        mutableListOf(Tile(4), Tile(2), Tile(4)),
//        mutableListOf(Tile(), Tile(), Tile(2)),
//        mutableListOf(Tile(4), Tile(2), Tile(2))
//    )
//
//    printArr(model.gameField)
//    println()
//
//    model.down()
//
//    printArr(model.gameField)
//}
//
//fun printArr(array: List<MutableList<Tile>>) {
//    array.forEach { println(it.map { tile -> tile.value }.joinToString(" ")) }
//}