package com.example.game_2024.view

import android.content.Context
import com.example.game_2024.R

data class Tile(val context: Context, var value: Int = 0) {

    fun isEmpty() = value == 0

    fun getFontColor(): Int {
        return context.resources.getColor(
            when (value) {
                0 -> R.color.font_0
                2 -> R.color.font_2
                4 -> R.color.font_4
                8 -> R.color.font_8
                16 -> R.color.font_16
                32 -> R.color.font_32
                64 -> R.color.font_64
                128 -> R.color.font_128
                256 -> R.color.font_256
                512 -> R.color.font_512
                1024 -> R.color.font_1024
                2048 -> R.color.font_2048
                else -> R.color.white
            },
            null
        )
    }

    fun getTileColor(): Int {
        return context.resources.getColor(
            when (value) {
                0 -> R.color.tile_0
                2 -> R.color.tile_2
                4 -> R.color.tile_4
                8 -> R.color.tile_8
                16 -> R.color.tile_16
                32 -> R.color.tile_32
                64 -> R.color.tile_64
                128 -> R.color.tile_128
                256 -> R.color.tile_256
                512 -> R.color.tile_512
                1024 -> R.color.tile_1024
                2048 -> R.color.tile_2048
                else -> R.color.black
            },
            null
        )
    }
}