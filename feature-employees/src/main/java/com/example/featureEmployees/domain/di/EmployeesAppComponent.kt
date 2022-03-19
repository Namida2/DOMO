package com.example.featureEmployees.domain.di

import com.example.core.domain.entities.Employee
import com.example.featureEmployees.domain.di.modules.ListenersModule
import com.example.featureEmployees.domain.di.modules.RepositoriesModule
import com.example.featureEmployees.domain.di.modules.ServicesModule
import com.example.featureEmployees.domain.services.EmployeesService
import com.example.featureEmployees.domain.useCases.DeleteEmployeeUseCase
import com.example.featureEmployees.domain.useCases.ReadEmployeesUseCase
import com.example.featureEmployees.domain.useCases.SetPermissionUseCase
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [RepositoriesModule::class, ServicesModule::class, ListenersModule::class],
    dependencies = [EmployeesAppComponentDeps::class]
)
@Singleton
interface EmployeesAppComponent {
    fun provideReadEmployeesUseCase(): ReadEmployeesUseCase
    fun provideSetPermissionUseCase(): SetPermissionUseCase
    fun provideDeleteEmployeeUseCase(): DeleteEmployeeUseCase
    fun provideEmployeesService(): EmployeesService
}

interface EmployeesAppComponentDeps {
    val currentEmployee: Employee?
}
