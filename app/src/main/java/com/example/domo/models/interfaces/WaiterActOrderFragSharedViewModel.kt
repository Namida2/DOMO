package com.example.domo.models.interfaces

import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub

interface WaiterActOrderFragSharedViewModel : OrdersService<OrdersServiceSub> {
//    override fun initCurrentOrder(tableId: Int, guestCount: Int)
}