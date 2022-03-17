package com.example.featureEmployees.domain.di.modules

import com.example.featureEmployees.data.EmployeesServiceImpl
import com.example.featureEmployees.domain.services.EmployeesService
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ServicesModule {
    @Binds
    @Singleton
    fun provideEmployeesService(service: EmployeesServiceImpl): EmployeesService
}