package com.example.feature_splashscreen.domain.di.modules

import com.example.waiter_core.data.database.Database
import com.example.waiter_core.data.database.daos.MenuDao
import dagger.Module
import dagger.Provides

@Module
class LocalRepositoryModule {
    @Provides
    fun provideMenuDao(database: Database): MenuDao = database.menuDao()
}