package com.example.domo.splashScreen.domain.di.modules

import com.example.domo.splashScreen.data.MenuRemoteRepository
import com.example.domo.splashScreen.data.MenuRemoteRepositoryImpl
import com.example.domo.splashScreen.data.UsersRemoteRepository
import com.example.domo.splashScreen.data.UsersRemoteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RemoteRepositoryModule {
    @Binds
    fun bindMenuRemoteRepository(repository: MenuRemoteRepositoryImpl): MenuRemoteRepository
    @Binds
    fun bindUsersRemoteRepository(repository: UsersRemoteRepositoryImpl) : UsersRemoteRepository
}