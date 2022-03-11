package com.example.featureRegistration.domain.di

import com.example.domo.registration.domain.GetPostItemsUseCase
import com.example.featureRegistration.domain.di.modules.RepositoriesModule
import com.example.featureRegistration.domain.di.modules.UseCasesModule
import com.example.featureRegistration.domain.useCases.RegistrationUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Component

@Component(
    modules = [UseCasesModule::class, RepositoriesModule::class],
    dependencies = [RegistrationAppComponentDeps::class]
)
interface RegistrationAppComponent {
    fun provideGetPostItemUseCase(): GetPostItemsUseCase
    fun provideRegistrationUseCase(): RegistrationUseCase
}

interface RegistrationAppComponentDeps {
    val firebaseAuth: FirebaseAuth
}