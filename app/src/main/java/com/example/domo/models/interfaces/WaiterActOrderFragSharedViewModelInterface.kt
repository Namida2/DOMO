package com.example.domo.models.interfaces

import entities.order.OrderItem

interface WaiterActOrderFragSharedViewModelInterface {
    fun initCurrentOrder(tableId: Int, guestCount: Int)
    fun getCurrentOrderItems(): Set<OrderItem>
}