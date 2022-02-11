package com.example.featureOrder.domain.di.modules

import com.example.featureOrder.domain.useCases.InsertOrderUseCase
import com.example.featureOrder.domain.useCases.InsertOrderUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun bindInsertOrderUseCase(useCase: InsertOrderUseCaseImpl): InsertOrderUseCase
}