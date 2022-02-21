package com.example.waiterCore.domain.order

import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewItem

data class OrderItem(
    var dishId: Int,
    var count: Int,
    var commentary: String,
    var isReady: Boolean = false,
) : BaseRecyclerViewItem {
    constructor() : this(0, 0, "")

    override fun equals(other: Any?): Boolean =
        if (other !is OrderItem) false
        else dishId == other.dishId && commentary == other.commentary

    override fun hashCode(): Int = "$$dishId: $commentary".hashCode()
}


