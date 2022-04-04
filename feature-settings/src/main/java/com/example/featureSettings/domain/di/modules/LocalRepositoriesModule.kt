package com.example.featureSettings.domain.di.modules

import com.example.core.data.database.Database
import com.example.core.data.database.daos.MenuDao
import com.example.core.data.repositorues.MenuLocalRepositoryImpl
import com.example.core.domain.repositories.MenuLocalRepository
import dagger.Module
import dagger.Provides

@Module
class LocalRepositoriesModule {
    @Provides
    fun provideMenuDao(database: Database): MenuDao = database.menuDao()
    @Provides
    fun provideMenuLocalRepository(
        menuDao: MenuDao,
    ): MenuLocalRepository =
        MenuLocalRepositoryImpl(menuDao)
}