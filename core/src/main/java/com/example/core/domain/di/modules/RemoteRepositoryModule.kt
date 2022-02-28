package com.example.core.domain.di.modules

import com.example.core.data.repositorues.OrdersRemoteRepository
import com.example.core.data.repositorues.OrdersRemoteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RemoteRepositoryModule {
    @Binds
    fun provideOrdersRemoteRepository(repository: OrdersRemoteRepositoryImpl): OrdersRemoteRepository
}
