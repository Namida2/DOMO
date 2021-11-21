package entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import entities.recyclerView.interfaces.BaseRecyclerViewItem

@Entity(tableName = "menu")
data class Dish(
    @PrimaryKey val id: Int = dishesCount,
    var name: String = "",
    val categoryName: String = "",
    val cost: String = "",
    val weight: String ="",
): BaseRecyclerViewItem {
    companion object {
        var dishesCount = 0
            get() = field++
    }
}