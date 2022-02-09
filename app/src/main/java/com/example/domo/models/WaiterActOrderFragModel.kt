package com.example.domo.models

import com.example.domo.models.interfaces.WaiterActOrderFragSharedViewModel
import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.OrderServiceSub
import javax.inject.Inject

class WaiterActOrderFragModel @Inject constructor(
    private val ordersService: OrdersService<OrderServiceSub>,
) : WaiterActOrderFragSharedViewModel,
    OrdersService<OrderServiceSub> by ordersService



