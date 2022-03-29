package com.example.featureSplashScreen.domain.di

import android.content.Context
import android.content.SharedPreferences
import com.example.administratorMain.domatn.di.AdminAppComponentDeps
import com.example.cookMain.domain.di.CookMainDeps
import com.example.core.data.database.Database
import com.example.core.domain.entities.Settings
import com.example.core.domain.entities.Employee
import com.example.core.domain.di.CoreAppComponentDeps
import com.example.core.domain.di.modules.ListenersModule
import com.example.core.domain.listeners.DeletedOrdersListener
import com.example.featureLogIn.domain.di.LogInDeps
import com.example.featureSplashScreen.domain.useCases.GetCurrentEmployeeUseCase
import com.example.featureSplashScreen.domain.useCases.ReadMenuUseCase
import com.example.featureSplashScreen.domain.di.modules.*
import com.example.featureSplashScreen.domain.useCases.ReadOrdersUseCase
import com.example.featureSplashScreen.domain.useCases.ReadSettingsUseCase
import com.example.waiterMain.domain.di.ProfileModuleDeps
import com.example.waiterMain.domain.di.WaiterMainDeps
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

//TODO: Add scopes for appComponents
@Singleton
@Component(
    modules = [UseCasesModule::class, LocalRepositoryModule::class,
        RemoteRepositoryModule::class, ServicesModule::class, FirebaseModule::class, ListenersModule::class],
)
interface SplashScreenAppComponent : WaiterMainDeps,
    CoreAppComponentDeps, CookMainDeps, ProfileModuleDeps, LogInDeps, AdminAppComponentDeps {

    @Component.Builder
    interface Builder {
        fun provideContext(@BindsInstance context: Context): Builder
        fun provideEmployee(@BindsInstance currentEmployee: Employee?): Builder
        fun provideDatabase(@BindsInstance database: Database): Builder
        fun provideSharedPreferences(@BindsInstance sharedPreferences: SharedPreferences): Builder
        fun build(): SplashScreenAppComponent
    }

    fun provideSettings(): Settings
    fun provideReadMenuUseCase(): ReadMenuUseCase
    fun provideReadOrdersUseCase(): ReadOrdersUseCase
    fun provideGetCurrentEmployeeUseCase(): GetCurrentEmployeeUseCase
    fun provideReadSettingsUseCase(): ReadSettingsUseCase
}
