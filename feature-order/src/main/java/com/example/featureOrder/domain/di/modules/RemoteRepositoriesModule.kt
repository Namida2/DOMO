package com.example.featureOrder.domain.di.modules

import com.example.featureOrder.domain.repositories.OrderMenuDialogRemoteRepository
import com.example.featureOrder.domain.repositories.OrderMenuDialogRemoteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RemoteRepositoriesModule {
    @Binds
    fun bindOrderMenuDialogRemoteRepository(
        repository: OrderMenuDialogRemoteRepositoryImpl,
    ): OrderMenuDialogRemoteRepository
}