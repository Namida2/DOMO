package com.example.featureOrder.domain.di

//TODO: lateinit property appComponent has not been initialized //STOPPED//
object OrderDepsStore {
    var deps: OrderAppComponentDeps? = null
    lateinit var appComponent: OrderAppComponent
}