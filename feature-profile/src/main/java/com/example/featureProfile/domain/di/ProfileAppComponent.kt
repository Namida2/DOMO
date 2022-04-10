package com.example.featureProfile.domain.di

import com.example.core.domain.entities.Employee
import com.example.core.domain.useCases.LeaveAccountUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Component

@Component(dependencies = [ProfileAppComponentDeps::class])
interface ProfileAppComponent {
    fun provideLeaveAccountUseCase(): LeaveAccountUseCase
}
interface ProfileAppComponentDeps {
    val currentEmployee: Employee?
    val firebaseAuth: FirebaseAuth
}