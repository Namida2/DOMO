package com.example.domo.splashScreen.domain.di

import android.content.Context
import android.content.SharedPreferences
import com.example.domo.authorization.domain.LogInUseCase

import com.example.domo.registration.domain.GetPostItemsUseCase
import com.example.domo.registration.domain.RegistrationUseCase
import com.example.domo.splashScreen.domain.GetCurrentEmployeeUseCase
import com.example.domo.splashScreen.domain.ReadMenuUseCase
import com.example.domo.splashScreen.domain.di.modules.LocalRepositoryModule
import com.example.domo.splashScreen.domain.di.modules.RemoteRepositoryModule
import com.example.domo.splashScreen.domain.di.modules.ServicesModule
import com.example.domo.splashScreen.domain.di.modules.UseCasesModule
import com.example.domo.splashScreen.presentation.ReadOrdersUseCase
import com.example.waiterCore.data.database.Database
import com.example.waiterCore.domain.menu.MenuService
import com.example.waiterMain.domain.di.WaiterMainDeps
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [UseCasesModule::class, LocalRepositoryModule::class, RemoteRepositoryModule::class, ServicesModule::class],
    dependencies = [SplashScreenAppComponentDeps::class] )
interface SplashScreenAppComponent: WaiterMainDeps {

    @Component.Builder
    interface Builder {
        fun provideDeps(deps: SplashScreenAppComponentDeps): Builder
        fun provideMenuService(@BindsInstance menuService: MenuService): Builder
        fun build(): SplashScreenAppComponent
    }

    fun provideLogInUseCase(): LogInUseCase
    fun provideReadMenuUseCase(): ReadMenuUseCase
    fun provideReadOrdersUseCase(): ReadOrdersUseCase
    fun provideRegistrationUseCase(): RegistrationUseCase
    fun provideGetPostItemsUseCase(): GetPostItemsUseCase
    fun provideGetCurrentEmployeeUseCase(): GetCurrentEmployeeUseCase

}

interface SplashScreenAppComponentDeps {
    val context: Context
    val sharedPreferences: SharedPreferences
    val database: Database
    val fireStore: FirebaseFirestore
    val auth: FirebaseAuth
}