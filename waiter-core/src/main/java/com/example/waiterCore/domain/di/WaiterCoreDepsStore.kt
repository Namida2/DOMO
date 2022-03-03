package com.example.waiterCore.domain.di

object WaiterCoreDepsStore {
    lateinit var deps: WaiterCoreAppComponentDeps
    val appComponent: WaiterCoreAppComponent by lazy {
        DaggerWaiterCoreAppComponent.builder()
            .waiterCoreAppComponentDeps(deps).build()
    }
}