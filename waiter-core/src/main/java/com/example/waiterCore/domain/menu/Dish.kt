package com.example.waiterCore.domain.menu

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewItem

@Entity(tableName = "menu")
data class Dish(
    @PrimaryKey val id: Int = 0,
    var name: String = "",
    val categoryName: String = "",
    val cost: String = "",
    val weight: String = "",
) : BaseRecyclerViewItem