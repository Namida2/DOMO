package com.example.waiterCore.domain.order

data class Order (
    var orderId: Int,
    var guestsCount: Int,
    var orderItems: MutableSet<OrderItem> = mutableSetOf(),
)