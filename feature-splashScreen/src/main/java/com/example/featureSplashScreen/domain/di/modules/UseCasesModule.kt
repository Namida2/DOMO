package com.example.featureSplashScreen.domain.di.modules

import com.example.featureSplashScreen.domain.useCases.GetCurrentEmployeeUseCase
import com.example.featureSplashScreen.domain.useCases.GetCurrentEmployeeUseCaseImpl
import com.example.core.domain.useCases.ReadMenuUseCase
import com.example.featureSplashScreen.domain.useCases.ReadOrdersUseCase
import com.example.featureSplashScreen.domain.useCases.ReadOrdersUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun bindGetCurrentEmployeeUseCase(useCase: GetCurrentEmployeeUseCaseImpl): GetCurrentEmployeeUseCase

    @Binds
    fun bindReadOrdersUseCase(useCase: ReadOrdersUseCaseImpl): ReadOrdersUseCase
}