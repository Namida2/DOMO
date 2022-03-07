package com.example.core.domain.order

import com.example.core.domain.recyclerView.interfaces.BaseRecyclerViewType
import com.example.core.domain.tools.extensions.logD

data class Order(
    var orderId: Int,
    var guestsCount: Int,
    var orderItems: MutableList<OrderItem> = mutableListOf(),
) : BaseRecyclerViewType