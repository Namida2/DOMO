package com.example.waiterCore.domain.order

import androidx.room.Entity
import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewItem

data class OrderItem(
//    val tableId: Int, //for local data source
    override val dishId: Int,
    override val count: Int,
    override val commentary: String,
    override val isReady: Boolean = false,
) : BaseRecyclerViewItem, BaseOrderItem {
    constructor() : this(0, 0, "")

    override fun equals(other: Any?): Boolean =
        if (other !is OrderItem) false
        else dishId == other.dishId && commentary == other.commentary

    override fun hashCode(): Int = "$$dishId: $commentary".hashCode()
}

//for fireStore
interface BaseOrderItem {
    val dishId: Int
    val count: Int
    val commentary: String
    val isReady: Boolean
}

