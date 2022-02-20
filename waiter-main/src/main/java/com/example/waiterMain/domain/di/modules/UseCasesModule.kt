package com.example.waiterMain.domain.di.modules

import com.example.waiterMain.domain.useCases.ReadNewOrderUseCase
import com.example.waiterMain.domain.useCases.ReadNewOrderUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun bindReadNewOrderUseCase(useCase: ReadNewOrderUseCaseImpl): ReadNewOrderUseCase
}