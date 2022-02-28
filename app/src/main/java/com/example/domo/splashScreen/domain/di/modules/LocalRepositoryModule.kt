package com.example.domo.splashScreen.domain.di.modules

import com.example.domo.authorization.data.UsersRemoteRepository
import com.example.domo.authorization.data.UsersRemoteRepositoryImpl
import com.example.domo.splashScreen.data.MenuLocalRepository
import com.example.domo.splashScreen.data.MenuLocalRepositoryImpl
import com.example.core.data.database.Database
import com.example.core.data.database.daos.EmployeeDao
import com.example.core.data.database.daos.MenuDao
import com.google.firebase.auth.FirebaseAuth
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
        menuDao: MenuDao,
    ): MenuLocalRepository =
        MenuLocalRepositoryImpl(menuDao)

    @Provides
    fun provideUsersRemoteRepository(auth: FirebaseAuth): UsersRemoteRepository =
        UsersRemoteRepositoryImpl(auth)

}