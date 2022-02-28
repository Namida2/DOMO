package com.example.core.domain.di.modules

import com.example.core.domain.useCases.ReadNewOrderUseCase
import com.example.core.domain.useCases.ReadNewOrderUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun bindReadNewOrderUseCase(useCase: ReadNewOrderUseCaseImpl): ReadNewOrderUseCase
}
