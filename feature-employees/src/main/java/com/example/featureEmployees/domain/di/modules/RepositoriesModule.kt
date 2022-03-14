package com.example.featureEmployees.domain.di.modules

import com.example.featureEmployees.data.repositories.EmployeesRepositoryImpl
import com.example.featureEmployees.domain.repositories.EmployeesRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoriesModule {
    @Binds
    fun bindEmployeesRepository(repository: EmployeesRepositoryImpl): EmployeesRepository
}