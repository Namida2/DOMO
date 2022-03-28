package com.example.core.domain.order

import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType

data class Order(
    var orderId: Int,
    var guestsCount: Int,
    var orderItems: MutableList<OrderItem> = mutableListOf(),
) : BaseRecyclerViewType {
    fun isCompleted(): Boolean = !orderItems.map {
        it.isReady
    }.contains(false)
}