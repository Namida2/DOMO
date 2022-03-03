package com.example.cookCore.domain.di.modules

import com.example.cookCore.data.repositories.OrderItemsRemoteRepository
import com.example.cookCore.data.repositories.OrderItemsRemoteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoriesModule {
    @Binds
    fun bindOrderItemsRemoteRepository(repository: OrderItemsRemoteRepositoryImpl): OrderItemsRemoteRepository
}