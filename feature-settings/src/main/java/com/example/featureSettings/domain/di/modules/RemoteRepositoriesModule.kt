package com.example.featureSettings.domain.di.modules

import com.example.featureSettings.data.remositories.MenuRemoteRepositoryImpl
import com.example.featureSettings.data.remositories.SettingRemoteRepositoryImpl
import com.example.featureSettings.domain.repositories.MenuRemoteRepository
import com.example.featureSettings.domain.repositories.SettingRemoteRepository
import dagger.Binds
import dagger.Module

@Module
interface RemoteRepositoriesModule {
    @Binds
    fun provideMenuRemoteRepository(repository: MenuRemoteRepositoryImpl): MenuRemoteRepository

    @Binds
    fun bindMenuRemoteRepository(
        repository: com.example.core.data.repositorues.MenuRemoteRepositoryImpl
    ): com.example.core.domain.repositories.MenuRemoteRepository

    @Binds
    fun bindSettingRemoteRepository(repository: SettingRemoteRepositoryImpl): SettingRemoteRepository
}