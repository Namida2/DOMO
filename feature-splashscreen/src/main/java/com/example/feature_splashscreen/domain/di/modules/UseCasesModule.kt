package com.example.feature_splashscreen.domain.di.modules

import com.example.feature_splashscreen.domain.GetCurrentEmployeeUseCase
import com.example.feature_splashscreen.domain.GetCurrentEmployeeUseCaseImpl
import com.example.feature_splashscreen.domain.ReadMenuUseCase
import com.example.feature_splashscreen.domain.ReadMenuUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun bindReadMenuUseCase(useCase: ReadMenuUseCaseImpl): ReadMenuUseCase
    @Binds
    fun bindGetCurrentEmployeeUseCase(useCase: GetCurrentEmployeeUseCaseImpl): GetCurrentEmployeeUseCase
}