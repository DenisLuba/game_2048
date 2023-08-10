package com.example.game_2024.model

import kotlin.math.max
import kotlin.random.Random

inline fun <T> arrayDequeOf(vararg elements: T) = ArrayDeque(elements.toList())
inline fun <T> ArrayDeque<T>.push(element: T) = addLast(element) // returns Unit
inline fun <T> ArrayDeque<T>.pop() = removeLastOrNull() // returns T?

class Model private constructor(val height: Int = 4, val width: Int = 4) {

//    **********************************************************************************************

//    SINGLETON

    companion object {
        @Volatile
        private var instance: Model? = null

        fun getInstance(height: Int = 4, width: Int = 4) =
            instance ?: synchronized(this) {
                instance ?: Model(height, width).also { instance = it }
            }
    }

//    **********************************************************************************************

//    Variables

    var score: Int = 0
    var maxTile: Int = 0
    private var isSaveNeeded = true

    var gameField: List<MutableList<Int>> = List(height) { MutableList(width) { 0 } }

    private val previousStates: ArrayDeque<List<MutableList<Int>>> = arrayDequeOf()
    private val previousScores: ArrayDeque<Int> = arrayDequeOf()

//    **********************************************************************************************

//    Constructor

    init {
        resetGameTiles()
    }

//    Methods

//    **********************************************************************************************

    private fun addRandomTile(field: List<MutableList<Int>>) {
        val tiles: MutableList<IntArray?> = getEmptyTiles(field).toMutableList()
        if (tiles.isNotEmpty()) {
            val randomTile = Random.nextInt(tiles.size)
            val row = tiles[randomTile]?.component1() ?: return
            val column = tiles[randomTile]?.component2() ?: return
            val num = if (Random.nextInt(10) < 9) 2 else 4
            field[row][column] = num
        }
    }

//    **********************************************************************************************

    private fun getEmptyTiles(field: List<MutableList<Int>>): List<IntArray?> =
        field.mapIndexed { row, list ->
            list.mapIndexed { column, num ->
                if (num == 0) intArrayOf(
                    row,
                    column
                ) else null
            }
        }.flatten().filterNotNull()

    //    **********************************************************************************************
    fun resetGameTiles() {
        for (list in gameField)
            for (i in list.indices)
                list[i] = 0

        previousStates.removeAll { true }
        previousScores.removeAll { true }
        isSaveNeeded = true
        score = 0
        maxTile = 0

        repeat(2) { addRandomTile(gameField) }
    }

//    **********************************************************************************************

    //    **********************************************************************************************
    fun rollback() {
        if (previousScores.isEmpty() || previousStates.isEmpty()) return
        gameField = previousStates.pop() ?: gameField
        score = previousScores.pop() ?: score
    }

//    Moving

    fun left() {
        if (gameField[0].size <= 1) return
        if (isSaveNeeded) saveState(gameField)
        var isChanged = false
        var i: Int
        var j: Int
        for (tiles in gameField) { // compress each list from the main list to the left
            i = 0
            j = 1
            while (j <= tiles.lastIndex) {
                if (tiles[j] != 0) {
                    if (tiles[i] == 0) {
                        tiles[i] = tiles[j]
                        tiles[j] = 0
                        isChanged = true
                    } else if (tiles[i] == tiles[j]) {
                        tiles[i] = tiles[i] shl 1
                        score += tiles[i] // score
                        maxTile = max(tiles[i], maxTile) // maxTile
                        i++
                        tiles[j] = 0
                        isChanged = true
                    } else if (j > i + 1) {
                        tiles[i + 1] = tiles[j]
                        i++
                        tiles[j] = 0
                        isChanged = true
                    } else i++
                }
                j++
            }
        }
        if (isChanged) { // if there were changes, then add a random tile and save the state
            addRandomTile(gameField)
            isSaveNeeded = true
        }
    }

    fun right() {
        if (gameField[0].size <= 1) return
        if (isSaveNeeded) saveState(gameField)
        var isChanged = false
        var i: Int
        var j: Int
        for (tiles in gameField) { // compress each list from the main list to the right
            i = tiles.lastIndex
            j = i - 1
            while (j >= 0) {
                if (tiles[j] != 0) {
                    if (tiles[i] == 0) {
                        tiles[i] = tiles[j]
                        tiles[j] = 0
                        isChanged = true
                    } else if (tiles[i] == tiles[j]) {
                        tiles[i] = tiles[i] shl 1
                        score += tiles[i] // score
                        maxTile = max(tiles[i], maxTile) // maxTile
                        i--
                        tiles[j] = 0
                        isChanged = true
                    } else if (j < i - 1) {
                        tiles[i - 1] = tiles[j]
                        i--
                        tiles[j] = 0
                        isChanged = true
                    } else i--
                }
                j--
            }
        }
        if (isChanged) { // if there were changes, then add a random tile and save the state
            addRandomTile(gameField)
            isSaveNeeded = true
        }
    }

    fun down() {
        if (gameField.size <= 1) return
        if (isSaveNeeded) saveState(gameField)
        var isChanged = false
        var y: Int
        var j: Int
        for (x in 0..gameField[0].lastIndex) {
            y = gameField.lastIndex
            j = y - 1
            while (j >= 0) {
                if (gameField[j][x] != 0) {
                    if (gameField[y][x] == 0) {
                        gameField[y][x] = gameField[j][x]
                        gameField[j][x] = 0
                        isChanged = true
                    } else if (gameField[y][x] == gameField[j][x]) {
                        gameField[y][x] = gameField[y][x] shl 1
                        score += gameField[y][x] // score
                        maxTile = max(gameField[y][x], maxTile) // maxTile
                        y--
                        gameField[j][x] = 0
                        isChanged = true
                    } else if (j < y - 1) {
                        gameField[y - 1][x] = gameField[j][x]
                        y--
                        gameField[j][x] = 0
                        isChanged = true
                    } else y--
                }
                j--
            }
        }
        if (isChanged) { // if there were changes, then add a random tile and save the state
            addRandomTile(gameField)
            isSaveNeeded = true
        }
    }

    fun up() {
        if (gameField.size <= 1) return
        if (isSaveNeeded) saveState(gameField)
        var isChanged = false
        var y: Int
        var j: Int
        for (x in 0..gameField[0].lastIndex) {
            y = 0
            j = 1
            while (j <= gameField.lastIndex) {
                if (gameField[j][x] != 0) {
                    if (gameField[y][x] == 0) {
                        gameField[y][x] = gameField[j][x]
                        gameField[j][x] = 0
                        isChanged = true
                    } else if (gameField[y][x] == gameField[j][x]) {
                        gameField[y][x] = gameField[y][x] shl 1
                        score += gameField[y][x] // score
                        maxTile = max(gameField[y][x], maxTile) // maxTile
                        y++
                        gameField[j][x] = 0
                        isChanged = true
                    } else if (j > y + 1) {
                        gameField[y + 1][x] = gameField[j][x]
                        y++
                        gameField[j][x] = 0
                        isChanged = true
                    } else y++
                }
                j++
            }
        }
        if (isChanged) { // if there were changes, then add a random tile and save the state
            addRandomTile(gameField)
            isSaveNeeded = true
        }
    }

//    **********************************************************************************************

//    Other additional functions for moving


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

//    **********************************************************************************************

//    Save state

    private fun saveState(tiles: List<MutableList<Int>>) {
        previousStates.push(tiles.map { it.toMutableList() }.toList())
        previousScores.push(score)
        isSaveNeeded = false
    }
}


fun main() {
    val model = Model.getInstance(3, 4)
    model.gameField = listOf(
        mutableListOf(0, 0, 4),
        mutableListOf(4, 2, 4),
        mutableListOf(2, 0, 2),
        mutableListOf(4, 2, 2)
    )

    println("ARRAY")
    model.gameField.forEach(::println)
    println()
    println("score = ${model.score}")
    println("maxTile = ${model.maxTile}")

    println()
    model.left()
    println("LEFT")
    model.gameField.forEach(::println)
    println()
    println("score = ${model.score}")
    println("maxTile = ${model.maxTile}")

//    model.gameField = listOf(
//        mutableListOf(4, 0, 4),
//        mutableListOf(4, 2, 4),
//        mutableListOf(4, 0, 2),
//        mutableListOf(4, 2, 0)
//    )
//
//    println("ARRAY")
//    model.gameField.forEach(::println)
//    println()

    println()
    model.right()
    println("RIGHT")
    model.gameField.forEach(::println)
    println()
    println("score = ${model.score}")
    println("maxTile = ${model.maxTile}")

//    model.gameField = listOf(
//        mutableListOf(4, 0, 4),
//        mutableListOf(4, 2, 4),
//        mutableListOf(4, 0, 2),
//        mutableListOf(4, 2, 0)
//    )
//
//    println("ARRAY")
//    model.gameField.forEach(::println)
//    println()

    println()
    model.down()
    println("DOWN")
    model.gameField.forEach(::println)
    println()
    println("score = ${model.score}")
    println("maxTile = ${model.maxTile}")

//    model.gameField = listOf(
//        mutableListOf(4, 0, 0),
//        mutableListOf(4, 2, 4),
//        mutableListOf(4, 0, 2),
//        mutableListOf(4, 2, 2)
//    )
//
//    println("ARRAY")
//    model.gameField.forEach(::println)
//    println()

    println()
    model.up()
    println("UP")
    model.gameField.forEach(::println)
    println()
    println("score = ${model.score}")
    println("maxTile = ${model.maxTile}")

    println()

    val list1 = listOf(
        mutableListOf(0, 0, 4),
        mutableListOf(4, 2, 4),
        mutableListOf(2, 0, 2),
        mutableListOf(4, 2, 2)
    )

    val list2 = list1.map { it.toMutableList() }.toList()

    println(list1 == list2)
    println(list1 === list2)

    list1.forEach(::println)
    println()
    list2.forEach(::println)
    println()

    list2[1][1] = 1000
    list1.forEach(::println)
    println()
    list2.forEach(::println)
    println()
}


