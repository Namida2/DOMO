package com.example.core.domain.menu

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType

@Entity(tableName = "menu")
data class Dish(
    @PrimaryKey val id: Int = 0,
    var name: String = "",
    val categoryName: String = "",
    val cost: String = "",
    val weight: String = "",
) : BaseRecyclerViewType, Comparable<Dish> {
    override fun compareTo(other: Dish): Int = id - other.id
}