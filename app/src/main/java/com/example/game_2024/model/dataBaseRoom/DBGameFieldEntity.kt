package com.example.game_2024.model.dataBaseRoom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.game_2024.model.Model
import kotlin.math.max

@Entity(tableName = "fields")
data class DBGameFieldEntity(
    @PrimaryKey val id: Int,
    val field: List<MutableList<Int>>,
    @ColumnInfo(name = "max_score") val maxScore: Int
) {

    fun toModel(): Model = Model.getInstance(
        height = field.size,
        width = field[0].size,
        _id = id
    ).apply { gameField = field }

    companion object {
        fun fromModel(model: Model): DBGameFieldEntity = DBGameFieldEntity(
            id = model.id,
            field = model.gameField,
            maxScore = model.maxScore
        )
    }
}