package com.example.feature_splashscreen.domain.di.modules

import com.example.feature_splashscreen.data.MenuLocalRepository
import com.example.feature_splashscreen.data.MenuLocalRepositoryImpl
import com.example.feature_splashscreen.domain.MenuService
import com.example.waiter_core.data.database.Database
import com.example.waiter_core.data.database.daos.MenuDao
import dagger.Module
import dagger.Provides

@Module
class LocalRepositoryModule {
    @Provides
    fun provideMenuDao(database: Database): MenuDao = database.menuDao()

    @Provides
    fun provideMenuLocalRepository(
        menuService: MenuService,
        menuDao: MenuDao
    ): MenuLocalRepository =
        MenuLocalRepositoryImpl(
            menuService,
            menuDao
        )
}