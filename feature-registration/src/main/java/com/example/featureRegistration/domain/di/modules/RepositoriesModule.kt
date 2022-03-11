package com.example.featureRegistration.domain.di.modules

import com.example.featureRegistration.data.RegistrationRemoteRepository
import com.example.featureRegistration.data.RegistrationRemoteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoriesModule {
    @Binds
    fun provideRegistrationRemoteRepository(repository: RegistrationRemoteRepositoryImpl): RegistrationRemoteRepository
}
