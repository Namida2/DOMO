package com.example.featureOrder.domain.di

object OrderDepsStore {
    lateinit var deps: OrderAppComponentDeps
    val appComponent: OrderAppComponent by lazy {
         DaggerOrderAppComponent.builder()
            .provideOrderAppComponentDeps(deps).build()
    }
}