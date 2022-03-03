package com.example.waiterCore.domain.di

import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import dagger.Component

@Component(dependencies = [WaiterCoreAppComponentDeps::class])
interface WaiterCoreAppComponent {

}
interface WaiterCoreAppComponentDeps {
    val ordersService: OrdersService<OrdersServiceSub>
}