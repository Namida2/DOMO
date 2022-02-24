package com.example.featureCurrentOrders.domain.di

import com.example.featureCurrentOrders.presentation.currentOrders.CurrentOrdersFragment
import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.OrdersServiceSub
import dagger.Component

@Component (dependencies = [CurrentOrdersAppComponentDeps::class])
interface CurrentOrdersAppComponent {

    fun inject(fragment: CurrentOrdersFragment)
}
interface CurrentOrdersAppComponentDeps {
    val ordersService: OrdersService<OrdersServiceSub>
}