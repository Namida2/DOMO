package com.example.featureSplashScreen.domain.di.modules

import com.example.featureSplashScreen.data.MenuRemoteRepositoryImpl
import com.example.featureSplashScreen.domain.repositories.MenuRemoteRepository
import dagger.Binds
import dagger.Module

@Module
interface RemoteRepositoryModule {
    @Binds
    fun bindMenuRemoteRepository(repository: MenuRemoteRepositoryImpl): MenuRemoteRepository

}