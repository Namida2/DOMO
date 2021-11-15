package entities

import androidx.room.Entity

@Entity(tableName = "menu")
data class Dish(
    var name: String,
    val categoryName: String,
    val cost: String,
    val weight: String,
)