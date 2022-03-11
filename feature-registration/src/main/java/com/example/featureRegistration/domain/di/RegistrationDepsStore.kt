package com.example.featureRegistration.domain.di

object RegistrationDepsStore {
    lateinit var deps: RegistrationAppComponentDeps
    val appComponent: RegistrationAppComponent by lazy {
        DaggerRegistrationAppComponent.builder().registrationAppComponentDeps(deps).build()
    }
}