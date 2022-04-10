package com.example.featureLogIn.domain.di

import com.example.core.domain.di.modules.AdminPasswordModule
import com.example.core.domain.useCases.LeaveAccountUseCase
import com.example.core.domain.useCases.ReadAdminPasswordUseCase
import com.example.featureLogIn.domain.LogInUseCase
import com.example.featureLogIn.domain.di.modules.RepositoriesModule
import com.example.featureLogIn.domain.di.modules.UseCasesModule
import com.google.firebase.auth.FirebaseAuth
import dagger.Component

@Component(
    dependencies = [LogInDeps::class],
    modules = [UseCasesModule::class, RepositoriesModule::class, AdminPasswordModule::class]
)
interface LogInAppComponent {
    fun provideLogInUseCase(): LogInUseCase
    fun provideReadAdminPasswordUseCase(): ReadAdminPasswordUseCase
    fun provideLeaveAccountUseCase(): LeaveAccountUseCase
}

interface LogInDeps {
    val firebaseAuth: FirebaseAuth
}