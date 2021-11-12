package entities

import androidx.room.Entity

@Entity(tableName = "dishes")
data class Dish(
    var name: String,
    val categoryName: String,
    val cost: String,
    val weight: String,
)