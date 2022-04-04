package com.example.featureSplashScreen.domain.di.modules

import com.example.core.data.repositorues.MenuRemoteRepositoryImpl
import com.example.featureSplashScreen.data.OrdersRemoteRepositoryImpl
import com.example.featureSplashScreen.data.SettingsRemoteRepositoryImpl
import com.example.core.domain.repositories.MenuRemoteRepository
import com.example.featureSplashScreen.domain.repositories.OrdersRemoteRepository
import com.example.featureSplashScreen.domain.repositories.SettingsRemoteRepository
import dagger.Binds
import dagger.Module

@Module
interface RemoteRepositoryModule {
    @Binds
    fun bindMenuRemoteRepository(repository: MenuRemoteRepositoryImpl): MenuRemoteRepository
    @Binds
    fun bindOrdersRemoteRepository(repository: OrdersRemoteRepositoryImpl): OrdersRemoteRepository
    @Binds
    fun bindSettingsRemoteRepository(repository: SettingsRemoteRepositoryImpl): SettingsRemoteRepository
}