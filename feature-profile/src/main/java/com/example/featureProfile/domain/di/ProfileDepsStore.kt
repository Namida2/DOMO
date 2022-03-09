package com.example.featureProfile.domain.di

object ProfileDepsStore {
    lateinit var deps: ProfileAppComponentDeps
    val appComponent: ProfileAppComponent by lazy {
        DaggerProfileAppComponent.builder().profileAppComponentDeps(deps).build()
    }
}