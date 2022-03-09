package com.example.waiterMain.domain.di

import com.example.featureProfile.domain.di.ProfileAppComponentDeps
import com.google.firebase.auth.FirebaseAuth

object WaiterMainDepsStore {
    lateinit var deps: WaiterMainDeps
    lateinit var profileDeps: ProfileModuleDeps
    val appComponent: WaiterMainAppComponent by lazy {
        DaggerWaiterMainAppComponent.builder().provideWaiterMainDeps(deps).build()
    }
}
interface ProfileModuleDeps {
    val firebaseAuth: FirebaseAuth
}
