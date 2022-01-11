package com.example.waiter_core.domain.order

data class Order (
    var tableId: Int,
    var guestsCount: Int,
    var orderItems: MutableSet<OrderItem> = mutableSetOf(),
)