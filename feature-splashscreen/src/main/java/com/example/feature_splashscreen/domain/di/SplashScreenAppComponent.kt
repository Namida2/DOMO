package com.example.feature_splashscreen.domain.di

import android.content.SharedPreferences
import com.example.feature_splashscreen.domain.ReadMenuUseCase
import com.example.feature_splashscreen.domain.di.modules.LocalRepositoryModule
import com.example.feature_splashscreen.domain.di.modules.RemoteRepositoryModule
import com.example.feature_splashscreen.domain.di.modules.UseCasesModule
import com.example.waiter_core.data.database.Database
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [UseCasesModule::class, LocalRepositoryModule::class, RemoteRepositoryModule::class],
    dependencies = [SplashScreenAppComponentDeps::class] )
interface SplashScreenAppComponent {
    fun provideReadMenuUseCase(): ReadMenuUseCase

    @Component.Builder
    interface Builder {
        fun putDeps(deps: SplashScreenAppComponentDeps): Builder
        fun build(): SplashScreenAppComponent
    }
}

interface SplashScreenAppComponentDeps {
    val sharedPreferences: SharedPreferences
    val database: Database
    val fireStore: FirebaseFirestore
    val auth: FirebaseFirestore
}