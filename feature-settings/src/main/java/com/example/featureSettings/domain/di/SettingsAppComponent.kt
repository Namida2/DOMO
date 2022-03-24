package com.example.featureSettings.domain.di

import com.example.core.domain.Settings
import com.example.core.domain.entities.Employee
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrdersServiceSub
import com.example.featureSettings.domain.di.modules.RepositoriesModule
import com.example.featureSettings.domain.useCases.SaveMenuUseCase
import dagger.Component

@Component(dependencies = [SettingsAppComponentDeps::class], modules = [RepositoriesModule::class])
interface SettingsAppComponent : SettingsAppComponentDeps {
    fun provideSaveMenuUseCase(): SaveMenuUseCase
}

interface SettingsAppComponentDeps {
    val settings: Settings
    val currentEmployee: Employee?
    val ordersService: OrdersService<OrdersServiceSub>
}
