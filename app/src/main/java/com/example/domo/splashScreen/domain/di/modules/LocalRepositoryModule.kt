package com.example.domo.splashScreen.domain.di.modules

import com.example.domo.splashScreen.data.MenuLocalRepository
import com.example.domo.splashScreen.data.MenuLocalRepositoryImpl
import com.example.domo.splashScreen.domain.MenuService
import com.example.waiter_core.data.database.Database
import com.example.waiter_core.data.database.daos.EmployeeDao
import com.example.waiter_core.data.database.daos.MenuDao
import dagger.Module
import dagger.Provides

@Module
class LocalRepositoryModule {
    @Provides
    fun provideMenuDao(database: Database): MenuDao = database.menuDao()

    @Provides
    fun provideEmployeeDao(database: Database): EmployeeDao = database.employeeDao()

    @Provides
    fun provideMenuLocalRepository(
        menuService: MenuService,
        menuDao: MenuDao,
    ): MenuLocalRepository =
        MenuLocalRepositoryImpl(menuService, menuDao)
}