package com.example.featureSplashScreen.domain.di.modules

import com.example.core.data.database.Database
import com.example.core.data.database.daos.EmployeeDao
import com.example.core.data.database.daos.MenuDao
import com.example.featureSplashScreen.data.MenuLocalRepositoryImpl
import com.example.featureSplashScreen.data.UsersRemoteRepositoryImpl
import com.example.featureSplashScreen.domain.repositories.MenuLocalRepository
import com.example.featureSplashScreen.domain.repositories.UsersRemoteRepository
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