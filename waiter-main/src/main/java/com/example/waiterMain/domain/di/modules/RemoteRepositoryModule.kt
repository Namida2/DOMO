package com.example.waiterMain.domain.di.modules

import com.example.waiterMain.data.OrdersRemoteRepository
import com.example.waiterMain.data.OrdersRemoteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RemoteRepositoryModule {
    @Binds
    fun provideOrdersRemoteRepository(repository: OrdersRemoteRepositoryImpl) : OrdersRemoteRepository
}
