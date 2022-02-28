package com.example.waiterMain.domain.di

object WaiterMainDepsStore {
    lateinit var deps: WaiterMainDeps
    val appComponent: WaiterMainAppComponent by lazy {
        DaggerWaiterMainAppComponent.builder().provideWaiterMainDeps(deps).build()
    }
}
