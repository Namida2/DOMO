package com.example.featureOrder.domain.di.modules

import com.example.featureOrder.presentation.order.doalogs.orderMenuDialog.useCases.InsertOrderUseCase
import com.example.featureOrder.presentation.order.doalogs.orderMenuDialog.useCases.InsertOrderUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun bindInsertOrderUseCase(useCase: InsertOrderUseCaseImpl): InsertOrderUseCase
}