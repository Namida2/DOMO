package com.example.domo.models

import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.domo.models.interfaces.WaiterActOrderFragSharedViewModel
import javax.inject.Inject

class WaiterActOrderFragModel @Inject constructor(
    private val ordersService: OrdersService<OrdersServiceSub>,
) : WaiterActOrderFragSharedViewModel,
    OrdersService<OrdersServiceSub> by ordersService



