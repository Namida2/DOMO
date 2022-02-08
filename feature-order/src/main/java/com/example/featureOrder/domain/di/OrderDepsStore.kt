package com.example.featureOrder.domain.di

object OrderDepsStore {
//    var deps: OrderAppComponentDeps? = null
    var appComponent: OrderAppComponent = DaggerOrderAppComponent.builder().build()
}