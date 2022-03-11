package com.example.featureLogIn.domain.di.modules

import com.example.featureLogIn.domain.LogInUseCase
import com.example.featureLogIn.domain.LogInUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun provideLogInUseCase(useCase: LogInUseCaseImpl): LogInUseCase
}