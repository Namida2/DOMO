package com.example.core.domain.order

import com.example.core.domain.recyclerView.interfaces.BaseRecyclerViewType

data class Order(
    var orderId: Int,
    var guestsCount: Int,
    var orderItems: MutableSet<OrderItem> = mutableSetOf(),
) : BaseRecyclerViewType