package com.example.waiterCore.domain.order

import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewType

data class OrderType(
    var dishId: Int,
    var count: Int,
    var commentary: String,
    var isReady: Boolean = false,
) : BaseRecyclerViewType {
    constructor() : this(0, 0, "")

    override fun equals(other: Any?): Boolean =
        if (other !is OrderType) false
        else dishId == other.dishId && commentary == other.commentary

    override fun hashCode(): Int = "$$dishId: $commentary".hashCode()
}


