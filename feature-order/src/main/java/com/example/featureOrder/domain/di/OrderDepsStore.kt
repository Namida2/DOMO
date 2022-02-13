package com.example.featureOrder.domain.di

import java.lang.IllegalArgumentException

object OrderDepsStore {
    var deps: OrderAppComponentDeps? = null
    set(value) {
        if (value == null) throw IllegalArgumentException()
        field = value
        appComponent = DaggerOrderAppComponent.builder()
            .provideOrderAppComponentDeps(value).build()
    }
    lateinit var appComponent: OrderAppComponent
}