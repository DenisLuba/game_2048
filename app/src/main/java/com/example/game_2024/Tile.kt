package com.example.game_2024

data class Tile(val value: Int = 0) {

    fun isEmpty() = value == 0

    fun getFontColor() = when (value) {
        0 -> R.color.Rich_black
        2 -> R.color.Rich_black2
        4 -> R.color.Chocolate_cosmos
        8 -> R.color.Rosewood
        16 -> R.color.Penn_red
        32 -> R.color.Engineering_orange
        64 -> R.color.Sinopia
        128 -> R.color.Persimmon
        256 -> R.color.Princeton_orange
        512 -> R.color.Orange
        1024 -> R.color.Selective_yellow
        2048 -> R.color.Icterine
        else -> R.color.White
    }

    fun getTitleColor(): Int = when (value) {
        0 -> R.color.Cream
        2 -> R.color.Mindaro
        4 -> R.color.Light_green
        8 -> R.color.Light_green2
        16 -> R.color.Emerald
        32 -> R.color.Keppel
        64 -> R.color.Verdigris
        128 -> R.color.Bondi_blue
        256 -> R.color.Cerulean
        512 -> R.color.Lapis_Lazuli
        1024 -> R.color.Indigo_dye
        2048 -> R.color.Penn_Blue
        else -> R.color.Black
    }
}