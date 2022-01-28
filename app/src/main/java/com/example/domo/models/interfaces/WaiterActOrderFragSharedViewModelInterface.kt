package com.example.domo.models.interfaces

import com.example.domo.models.OrderServiceSub
import entities.interfaces.OrderServiceInterface
import com.example.waiterCore.domain.order.OrderItem

interface WaiterActOrderFragSharedViewModelInterface: OrderServiceInterface<OrderServiceSub>{
    fun getCurrentOrderItems(): Set<OrderItem>
}