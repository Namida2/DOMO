package com.example.feature_splashscreen.domain.di.modules

import com.example.feature_splashscreen.data.MenuRemoteRepository
import com.example.feature_splashscreen.data.MenuRemoteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RemoteRepositoryModule {
    @Binds
    fun bindMenuRemoteRepository(repository: MenuRemoteRepositoryImpl): MenuRemoteRepository
}