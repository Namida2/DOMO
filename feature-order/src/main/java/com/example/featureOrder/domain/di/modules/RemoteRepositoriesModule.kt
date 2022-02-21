package com.example.featureOrder.domain.di.modules

import com.example.featureOrder.data.repositories.OrdersRemoteRepository
import com.example.featureOrder.data.repositories.OrdersRemoteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RemoteRepositoriesModule {
    @Binds
    fun bindOrderMenuDialogRemoteRepository(
        repository: OrdersRemoteRepositoryImpl,
    ): OrdersRemoteRepository
}