package com.example.core.domain.di

object CoreDepsStore {
    lateinit var deps: CoreAppComponentDeps
    val appComponent: CoreAppComponent by lazy {
        DaggerCoreAppComponent.builder().coreAppComponentDeps(deps).build()
    }
}