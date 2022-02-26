package com.example.waiterCore.domain.order

import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewType

data class Order (
    var orderId: Int,
    var guestsCount: Int,
    var orderItems: MutableSet<OrderItem> = mutableSetOf(),
): BaseRecyclerViewType