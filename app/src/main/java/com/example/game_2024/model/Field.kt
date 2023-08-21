package com.example.game_2024.model

data class Field(
    val score: Int,
    val maxScore: Int,
    val field: List<MutableList<Int>>
)