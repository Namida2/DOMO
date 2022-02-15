package com.example.waiterCore.domain.order

data class Order (
    var tableId: Int,
    var guestsCount: Long,
    var orderItems: MutableSet<OrderItem> = mutableSetOf(),
)