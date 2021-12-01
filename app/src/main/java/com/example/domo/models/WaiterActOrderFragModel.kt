package com.example.domo.models

import com.example.domo.models.interfaces.WaiterActOrderFragSharedViewModelInterface
import entities.interfaces.OrderServiceInterface
import entities.order.OrderItem
import javax.inject.Inject

class WaiterActOrderFragModel @Inject constructor (
    private val orderService: OrderServiceInterface<OrderServiceSub>
): WaiterActOrderFragSharedViewModelInterface {
    override fun initCurrentOrder(tableId: Int, guestCount: Int) {
        orderService.initCurrentOrder(tableId, guestCount)
    }
    override fun getCurrentOrderItems(): Set<OrderItem>
    = orderService.currentOrder?.orderItems?.toSet()!!
}