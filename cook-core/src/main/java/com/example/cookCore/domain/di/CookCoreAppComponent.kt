package com.example.cookCore.domain.di

import com.example.cookCore.domain.di.modules.RepositoriesModule
import com.example.cookCore.domain.di.modules.UseCasesModule
import com.example.cookCore.domain.useCases.ChangeOrderItemStateUseCase
import dagger.Component

@Component(modules = [UseCasesModule::class, RepositoriesModule::class])
interface CookCoreAppComponent {
    fun provideChangeOrderItemStateUseCase(): ChangeOrderItemStateUseCase
}
