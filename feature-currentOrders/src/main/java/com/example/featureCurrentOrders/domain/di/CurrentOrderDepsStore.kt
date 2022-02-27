package com.example.featureCurrentOrders.domain.di

object CurrentOrderDepsStore {
    lateinit var deps: CurrentOrdersAppComponentDeps
    val appComponent: CurrentOrdersAppComponent by lazy {
        DaggerCurrentOrdersAppComponent.builder()
            .provideCurrentOrdersDeps(deps)
            .build()
    }
}