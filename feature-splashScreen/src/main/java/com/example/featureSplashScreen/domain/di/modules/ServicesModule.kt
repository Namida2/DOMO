package com.example.featureSplashScreen.domain.di.modules

import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.entities.order.OrdersServiceImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ServicesModule {
    @Binds
    @Singleton
    fun bindOrderService(service: OrdersServiceImpl): OrdersService
}