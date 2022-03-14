package com.example.featureLogIn.domain.di

object LogInDepsStore {
    lateinit var deps: LogInDeps
    val appComponent: LogInAppComponent by lazy {
        DaggerLogInAppComponent.builder().logInDeps(deps).build()
    }
}