package com.example.featureCurrentOrders.domain.di

import com.example.core.domain.Employee
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.featureCurrentOrders.presentation.currentOrders.CurrentOrdersFragment
import dagger.Component

@Component(dependencies = [CurrentOrdersAppComponentDeps::class])
interface CurrentOrdersAppComponent {

    @Component.Builder
    interface Builder {
        fun provideCurrentOrdersDeps(deps: CurrentOrdersAppComponentDeps): Builder
        fun build(): CurrentOrdersAppComponent
    }

    fun inject(fragment: CurrentOrdersFragment)
}

interface CurrentOrdersAppComponentDeps {
    val currentEmployee: Employee?
    val ordersService: OrdersService<OrdersServiceSub>
}