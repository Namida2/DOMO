package entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu")
data class Dish(
    @PrimaryKey val id: Int = dishesCount,
    var name: String = "",
    val categoryName: String = "",
    val cost: String = "",
    val weight: String ="",
) {
    companion object {
        var dishesCount = 0
            get() = field++
    }
}