package com.example.featureRegistration.domain.di

import com.example.core.domain.di.modules.AdminPasswordModule
import com.example.core.domain.useCases.LeaveAccountUseCase
import com.example.core.domain.useCases.ReadAdminPasswordUseCase
import com.example.featureRegistration.domain.useCases.GetPostItemsUseCase
import com.example.featureRegistration.domain.di.modules.RepositoriesModule
import com.example.featureRegistration.domain.di.modules.UseCasesModule
import com.example.featureRegistration.domain.useCases.RegistrationUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Component

@Component(
    modules = [UseCasesModule::class, RepositoriesModule::class, AdminPasswordModule::class],
    dependencies = [RegistrationAppComponentDeps::class]
)
interface RegistrationAppComponent {
    fun provideGetPostItemUseCase(): GetPostItemsUseCase
    fun provideRegistrationUseCase(): RegistrationUseCase
    fun provideReadAdminPasswordUseCase(): ReadAdminPasswordUseCase
    fun provideLeaveAccountUseCase(): LeaveAccountUseCase
}

interface RegistrationAppComponentDeps {
    val firebaseAuth: FirebaseAuth
}