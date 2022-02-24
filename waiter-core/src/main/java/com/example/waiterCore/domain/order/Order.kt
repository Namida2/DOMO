package com.example.waiterCore.domain.order

data class Order (
    var tableId: Int,
    var guestsCount: Int,
    var orderItems: MutableSet<OrderType> = mutableSetOf(),
)