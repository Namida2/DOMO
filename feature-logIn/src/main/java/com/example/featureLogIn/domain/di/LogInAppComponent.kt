package com.example.featureLogIn.domain.di

import com.example.featureLogIn.domain.LogInUseCase
import com.example.featureLogIn.domain.di.modules.RepositoriesModule
import com.example.featureLogIn.domain.di.modules.UseCasesModule
import com.google.firebase.auth.FirebaseAuth
import dagger.Component

@Component(dependencies = [LogInAppComponentDeps::class], modules = [UseCasesModule::class,RepositoriesModule::class])
interface LogInAppComponent {
    fun provideLogInUseCase(): LogInUseCase
}
interface LogInAppComponentDeps {
    val firebaseAuth: FirebaseAuth
}