package com.example.featureProfile.domain.di

import com.example.core.domain.Employee
import com.example.featureProfile.domain.LeaveAccountUseCase
import com.example.featureProfile.domain.di.modules.UseCasesModule
import com.google.firebase.auth.FirebaseAuth
import dagger.Component

@Component(modules = [UseCasesModule::class], dependencies = [ProfileAppComponentDeps::class])
interface ProfileAppComponent {
    fun provideLeaveAccountUseCase(): LeaveAccountUseCase
}
interface ProfileAppComponentDeps {
    val currentEmployee: Employee?
    val firebaseAuth: FirebaseAuth
}