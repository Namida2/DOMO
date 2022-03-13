package com.example.featureSplashScreen.domain.di.modules

import com.example.domo.splashScreen.data.MenuRemoteRepository
import com.example.domo.splashScreen.data.MenuRemoteRepositoryImpl
import com.example.featureSplashScreen.data.UsersRemoteRepository
import com.example.featureSplashScreen.data.UsersRemoteRepositoryImpl
import com.example.featureRegistration.data.RegistrationRemoteRepository
import com.example.featureRegistration.data.RegistrationRemoteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RemoteRepositoryModule {
    @Binds
    fun bindMenuRemoteRepository(repository: MenuRemoteRepositoryImpl): MenuRemoteRepository
    
    @Binds
    fun bindRegistrationRemoteRepository(repository: RegistrationRemoteRepositoryImpl): RegistrationRemoteRepository
}