package com.example.domo.models.interfaces

import com.example.domo.models.OrderServiceSub
import entities.interfaces.OrderServiceInterface
import entities.order.OrderItem

interface WaiterActOrderFragSharedViewModelInterface: OrderServiceInterface<OrderServiceSub>{
    fun getCurrentOrderItems(): Set<OrderItem>
}