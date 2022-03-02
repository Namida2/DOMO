package com.example.cookCore.domain.di.modules

import com.example.cookCore.domain.useCases.ChangeOrderItemStateUseCase
import com.example.cookCore.domain.useCases.ChangeOrderItemStateUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun bindChangeOrderItemStateUseCase(useCase: ChangeOrderItemStateUseCaseImpl): ChangeOrderItemStateUseCase
}