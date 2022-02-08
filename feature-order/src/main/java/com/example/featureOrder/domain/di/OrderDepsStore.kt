package com.example.featureOrder.domain.di

//TODO: late init property appComponent has not been initialized //STOPPED//
object OrderDepsStore {
//    var deps: OrderAppComponentDeps? = null
    var appComponent: OrderAppComponent = DaggerOrderAppComponent.builder().build()
}