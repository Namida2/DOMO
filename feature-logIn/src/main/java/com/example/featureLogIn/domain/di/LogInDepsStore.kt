package com.example.featureLogIn.domain.di

object LogInDepsStore {
    lateinit var deps: LogInAppComponentDeps
    val appComponent: LogInAppComponent by lazy {
        DaggerLogInAppComponent.builder().logInAppComponentDeps(deps).build()
    }
}