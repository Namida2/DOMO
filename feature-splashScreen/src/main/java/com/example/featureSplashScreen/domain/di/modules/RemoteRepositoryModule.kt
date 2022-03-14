package com.example.featureSplashScreen.domain.di.modules

import com.example.domo.splashScreen.data.MenuRemoteRepository
import com.example.domo.splashScreen.data.MenuRemoteRepositoryImpl

import dagger.Binds
import dagger.Module

@Module
interface RemoteRepositoryModule {
    @Binds
    fun bindMenuRemoteRepository(repository: MenuRemoteRepositoryImpl): MenuRemoteRepository

}