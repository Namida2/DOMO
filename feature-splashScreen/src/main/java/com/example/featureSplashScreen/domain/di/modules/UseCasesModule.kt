package com.example.featureSplashScreen.domain.di.modules

import com.example.featureSplashScreen.domain.GetCurrentEmployeeUseCase
import com.example.featureSplashScreen.domain.GetCurrentEmployeeUseCaseImpl
import com.example.featureSplashScreen.domain.ReadMenuUseCase
import com.example.featureSplashScreen.domain.ReadMenuUseCaseImpl
import com.example.featureSplashScreen.presentation.ReadOrdersUseCase
import com.example.featureSplashScreen.presentation.ReadOrdersUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun bindReadMenuUseCase(useCase: ReadMenuUseCaseImpl): ReadMenuUseCase

    @Binds
    fun bindGetCurrentEmployeeUseCase(useCase: GetCurrentEmployeeUseCaseImpl): GetCurrentEmployeeUseCase

    @Binds
    fun bindReadOrdersUseCase(useCase: ReadOrdersUseCaseImpl): ReadOrdersUseCase
}