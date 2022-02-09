package com.example.featureOrder.domain.di

import com.example.featureOrder.domain.di.modules.ServicesModule
import com.example.featureOrder.presentation.order.OrderFragment
import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.menu.MenuService
import com.example.waiterCore.domain.order.OrderServiceSub
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [], modules = [ServicesModule::class])
interface OrderAppComponent {
    @Component.Builder
    interface Builder {
//        fun provideOrderAppComponentDeps(deps: OrderAppComponentDeps): Builder
        fun build(): OrderAppComponent
    }
    fun inject(fragment: OrderFragment)
    fun provideOrderService() : OrdersService<OrderServiceSub>
}
