package com.example.featureLogIn.domain.di.modules

import com.example.featureLogIn.data.UsersRemoteRepository
import com.example.featureLogIn.data.UsersRemoteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoriesModule {
    @Binds
    fun bindUsersRemoteRepository(repository: UsersRemoteRepositoryImpl): UsersRemoteRepository
}