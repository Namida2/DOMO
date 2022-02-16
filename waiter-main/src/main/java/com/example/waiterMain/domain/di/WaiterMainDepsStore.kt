package com.example.waiterMain.domain.di

import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.OrdersServiceSub
import com.example.waiterCore.domain.tools.extensions.logD

object WaiterMainDepsStore {
    lateinit var deps: WaiterMainDeps
}

interface WaiterMainDeps {
    val ordersService: OrdersService<OrdersServiceSub>
}
