package com.example.administratorMain.domatn.di

import com.example.core.domain.Employee
import com.google.firebase.auth.FirebaseAuth
import dagger.Component

@Component(dependencies = [AdminAppComponentDeps::class])
interface AdminAppComponent

interface AdminAppComponentDeps {
    val currentEmployee: Employee?
    val firestoreAuth: FirebaseAuth
}