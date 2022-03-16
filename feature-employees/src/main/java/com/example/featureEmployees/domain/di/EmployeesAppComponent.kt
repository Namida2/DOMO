package com.example.featureEmployees.domain.di

import com.example.core.domain.Employee
import com.example.featureEmployees.domain.useCases.ReadEmployeesUseCase
import com.example.featureEmployees.domain.di.modules.RepositoriesModule
import com.example.featureEmployees.domain.useCases.DeleteEmployeeUseCase
import com.example.featureEmployees.domain.useCases.SetPermissionUseCase
import dagger.Component

@Component(modules = [RepositoriesModule::class], dependencies = [EmployeesAppComponentDeps::class])
interface EmployeesAppComponent {
    fun provideReadEmployeesUseCase(): ReadEmployeesUseCase
    fun provideSetPermissionUseCase(): SetPermissionUseCase
    fun provideDeleteEmployeeUseCase(): DeleteEmployeeUseCase
}
interface EmployeesAppComponentDeps {
    val currentEmployee: Employee?
}
