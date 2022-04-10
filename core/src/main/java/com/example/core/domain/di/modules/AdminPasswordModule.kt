package com.example.core.domain.di.modules

import com.example.core.data.repositorues.AdminPasswordRemoteRepositoryImpl
import com.example.core.domain.repositories.AdminPasswordRemoteRepository
import dagger.Binds
import dagger.Module

@Module
interface AdminPasswordModule {
    @Binds
    fun provideAdminPasswordRemoteRepository(repository: AdminPasswordRemoteRepositoryImpl): AdminPasswordRemoteRepository
}