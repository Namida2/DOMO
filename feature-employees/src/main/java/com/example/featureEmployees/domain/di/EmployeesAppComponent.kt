package com.example.featureEmployees.domain.di

import com.example.featureEmployees.domain.ReadEmployeesUseCase
import com.example.featureEmployees.domain.di.modules.RepositoriesModule
import dagger.Component

@Component(modules = [RepositoriesModule::class])
interface EmployeesAppComponent {
    fun provideReadEmployeesUseCase(): ReadEmployeesUseCase
}
