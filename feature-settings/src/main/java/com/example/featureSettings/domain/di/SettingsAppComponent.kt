package com.example.featureSettings.domain.di

import android.content.SharedPreferences
import com.example.core.data.database.Database
import com.example.core.domain.entities.Settings
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.constants.SharedPreferencesConstants
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.useCases.ReadMenuUseCase
import com.example.featureSettings.domain.di.modules.LocalRepositoriesModule
import com.example.featureSettings.domain.di.modules.RemoteRepositoriesModule
import com.example.featureSettings.domain.useCases.SaveMenuUseCase
import com.example.featureSettings.domain.useCases.SaveSettingsUseCase
import dagger.Component

@Component(dependencies = [SettingsAppComponentDeps::class], modules = [RemoteRepositoriesModule::class, LocalRepositoriesModule::class])
interface SettingsAppComponent : SettingsAppComponentDeps {
    fun provideSaveMenuUseCase(): SaveMenuUseCase
    fun provideReadMenuUseCase(): ReadMenuUseCase
    fun provideSaveSettingsUseCase(): SaveSettingsUseCase
}

interface SettingsAppComponentDeps {
    val settings: Settings
    val database: Database
    val sharedPreferences: SharedPreferences
    val currentEmployee: Employee?
    val ordersService: OrdersService
}
