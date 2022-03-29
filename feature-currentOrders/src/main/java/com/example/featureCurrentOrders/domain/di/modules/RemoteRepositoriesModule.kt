package com.example.featureCurrentOrders.domain.di.modules

import com.example.featureCurrentOrders.data.repositories.OrdersRemoteRepositoryImpl
import com.example.featureCurrentOrders.domain.repositories.OrdersRemoteRepository
import dagger.Binds
import dagger.Module

@Module
interface RemoteRepositoriesModule {
    @Binds
    fun provideOrdersRemoteRepository(repository: OrdersRemoteRepositoryImpl): OrdersRemoteRepository
}