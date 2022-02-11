package com.example.featureOrder.domain.di.modules

import com.example.featureOrder.data.repositories.OrderMenuDialogRemoteRepository
import com.example.featureOrder.data.repositories.OrderMenuDialogRemoteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RemoteRepositoriesModule {
    @Binds
    fun bindOrderMenuDialogRemoteRepository(
        repository: OrderMenuDialogRemoteRepositoryImpl,
    ): OrderMenuDialogRemoteRepository
}