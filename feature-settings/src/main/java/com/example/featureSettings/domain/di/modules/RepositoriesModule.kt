package com.example.featureSettings.domain.di.modules

import com.example.featureSettings.data.remositories.MenuRemoteRepositoryImpl
import com.example.featureSettings.domain.repositories.MenuRemoteRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoriesModule {
    @Binds
    fun provideMenuRemoteRepository(repository: MenuRemoteRepositoryImpl): MenuRemoteRepository
}