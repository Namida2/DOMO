package com.example.featureEmployees.domain.di.modules

import com.example.featureEmployees.data.listeners.NewEmployeesListenerImpl
import com.example.featureEmployees.domain.listeners.NewEmployeesListener
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ListenersModule {
    @Binds
    @Singleton
    fun provideNewEmployeeListener(listener: NewEmployeesListenerImpl): NewEmployeesListener
}