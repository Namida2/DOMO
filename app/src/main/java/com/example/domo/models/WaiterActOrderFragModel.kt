package com.example.domo.models

import com.example.domo.models.interfaces.WaiterActOrderFragSharedViewModelInterface
import entities.interfaces.OrderServiceInterface
import entities.order.OrderItem
import javax.inject.Inject

class WaiterActOrderFragModel @Inject constructor (
    private val orderService: OrderServiceInterface<OrderServiceSub>
): WaiterActOrderFragSharedViewModelInterface, OrderServiceInterface<OrderServiceSub> by orderService {

    override fun getCurrentOrderItems(): Set<OrderItem>
    = orderService.currentOrder?.orderItems?.toSet()!!

}
