package com.example.feature_splashscreen.domain.di

import android.content.SharedPreferences
import com.example.feature_splashscreen.domain.ReadMenuUseCase
import com.example.feature_splashscreen.domain.di.modules.LocalRepositoryModule
import com.example.feature_splashscreen.domain.di.modules.RemoteRepositoryModule
import com.example.feature_splashscreen.domain.di.modules.UseCasesModule
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [UseCasesModule::class, LocalRepositoryModule::class, RemoteRepositoryModule::class],
    dependencies = [SplashScreenAppComponentDeps::class] )
interface SplashScreenAppComponent {
    fun provideReadMenuUseCase(): ReadMenuUseCase
}

interface SplashScreenAppComponentDeps {
    val sharedPreferences: SharedPreferences
    val fireStore: FirebaseFirestore
    val auth: FirebaseFirestore
}