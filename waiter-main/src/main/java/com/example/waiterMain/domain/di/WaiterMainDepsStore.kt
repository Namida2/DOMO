package com.example.waiterMain.domain.di

import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.google.firebase.auth.FirebaseAuth

object WaiterMainDepsStore {
    lateinit var deps: WaiterMainDeps
    lateinit var profileDeps: ProfileModuleDeps
    lateinit var employeeAuthCallback: EmployeeAuthCallback
    val appComponent: WaiterMainAppComponent by lazy {
        DaggerWaiterMainAppComponent.builder()
            .provideWaiterMainDeps(deps)
            .build()
    }
}

interface ProfileModuleDeps {
    val firebaseAuth: FirebaseAuth
}
