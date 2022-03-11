package com.example.domo.splashScreen.domain.di

import android.content.Context
import android.content.SharedPreferences
import com.example.cookMain.domain.di.CookMainDeps
import com.example.core.data.database.Database
import com.example.core.domain.Employee
import com.example.core.domain.di.CoreAppComponentDeps
import com.example.domo.splashScreen.domain.GetCurrentEmployeeUseCase
import com.example.domo.splashScreen.domain.ReadMenuUseCase
import com.example.domo.splashScreen.domain.di.modules.LocalRepositoryModule
import com.example.domo.splashScreen.domain.di.modules.RemoteRepositoryModule
import com.example.domo.splashScreen.domain.di.modules.ServicesModule
import com.example.domo.splashScreen.domain.di.modules.UseCasesModule
import com.example.domo.splashScreen.presentation.ReadOrdersUseCase
import com.example.featureLogIn.domain.LogInUseCase
import com.example.featureLogIn.domain.di.LogInAppComponentDeps
import com.example.waiterMain.domain.di.ProfileModuleDeps
import com.example.waiterMain.domain.di.WaiterMainDeps
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [UseCasesModule::class, LocalRepositoryModule::class, RemoteRepositoryModule::class, ServicesModule::class],
    dependencies = [SplashScreenAppComponentDeps::class]
)
interface SplashScreenAppComponent : WaiterMainDeps,
    CoreAppComponentDeps, CookMainDeps, ProfileModuleDeps, LogInAppComponentDeps {

    @Component.Builder
    interface Builder {
        fun provideDeps(deps: SplashScreenAppComponentDeps): Builder
        fun build(): SplashScreenAppComponent
    }

    fun provideLogInUseCase(): LogInUseCase
    fun provideReadMenuUseCase(): ReadMenuUseCase
    fun provideReadOrdersUseCase(): ReadOrdersUseCase
    fun provideGetCurrentEmployeeUseCase(): GetCurrentEmployeeUseCase

}

interface SplashScreenAppComponentDeps {
    val currentEmployee: Employee?
    val context: Context
    val sharedPreferences: SharedPreferences
    val database: Database
    val fireStore: FirebaseFirestore
    val auth: FirebaseAuth
}