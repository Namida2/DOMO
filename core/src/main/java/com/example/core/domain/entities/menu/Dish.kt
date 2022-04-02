package com.example.core.domain.entities.menu

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType

@Entity(tableName = "menu")
data class Dish(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String = "",
    val categoryName: String = "",
    var cost: String = "",
    var weight: String = "",
) : BaseRecyclerViewType, Comparable<Dish> {
    override fun compareTo(other: Dish): Int = id - other.id
}