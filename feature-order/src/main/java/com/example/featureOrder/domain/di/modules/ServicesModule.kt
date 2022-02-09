package com.example.featureOrder.domain.di.modules

import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.order.OrderServiceSub
import com.example.waiterCore.domain.order.OrdersServiceImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ServicesModule {
    @Binds
    @Singleton
    fun bindOrderService(service: OrdersServiceImpl): OrdersService<OrderServiceSub>
}