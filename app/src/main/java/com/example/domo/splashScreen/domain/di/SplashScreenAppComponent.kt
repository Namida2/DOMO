package com.example.domo.splashScreen.domain.di

import android.content.SharedPreferences
import com.example.domo.splashScreen.domain.GetCurrentEmployeeUseCase
import com.example.domo.splashScreen.domain.ReadMenuUseCase
import com.example.domo.splashScreen.domain.di.modules.LocalRepositoryModule
import com.example.domo.splashScreen.domain.di.modules.RemoteRepositoryModule
import com.example.domo.splashScreen.domain.di.modules.UseCasesModule
import com.example.waiterCore.data.database.Database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [UseCasesModule::class, LocalRepositoryModule::class, RemoteRepositoryModule::class],
    dependencies = [SplashScreenAppComponentDeps::class] )
interface SplashScreenAppComponent {

    @Component.Builder
    interface Builder {
        fun putDeps(deps: SplashScreenAppComponentDeps): Builder
        fun build(): SplashScreenAppComponent
    }

    fun provideReadMenuUseCase(): ReadMenuUseCase
    fun provideGetCurrentEmployeeUseCase(): GetCurrentEmployeeUseCase

}

interface SplashScreenAppComponentDeps {
    val sharedPreferences: SharedPreferences
    val database: Database
    val fireStore: FirebaseFirestore
    val auth: FirebaseAuth
}