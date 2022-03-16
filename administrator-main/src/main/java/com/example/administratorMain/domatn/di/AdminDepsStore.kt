package com.example.administratorMain.domatn.di

import com.example.core.domain.Employee
import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.example.featureEmployees.domain.di.EmployeesAppComponentDeps
import com.example.featureEmployees.domain.di.EmployeesDepsStore
import com.example.featureProfile.domain.di.ProfileAppComponentDeps
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.google.firebase.auth.FirebaseAuth

object AdminDepsStore {
    lateinit var deps: AdminAppComponentDeps
    lateinit var employeeAuthCallback: EmployeeAuthCallback
    val appComponent: AdminAppComponent by lazy {
        DaggerAdminAppComponent.builder().adminAppComponentDeps(deps).build()
    }.also {
        provideProfileDeps()
        provideEmployeesDeps()
    }

    private fun provideProfileDeps() {
        ProfileDepsStore.deps = object : ProfileAppComponentDeps {
            override val currentEmployee: Employee?
                get() = deps.currentEmployee
            override val firebaseAuth: FirebaseAuth
                get() = deps.firestoreAuth
        }
    }

    private fun provideEmployeesDeps() {
        EmployeesDepsStore.deps = object : EmployeesAppComponentDeps {
            override val currentEmployee: Employee?
                get() = deps.currentEmployee
        }
    }

}