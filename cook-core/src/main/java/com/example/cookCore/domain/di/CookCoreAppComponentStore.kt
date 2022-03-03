package com.example.cookCore.domain.di

import com.example.cookCore.domain.di.DaggerCookCoreAppComponent

object CookCoreAppComponentStore {
    val cookCoreAppComponent: CookCoreAppComponent by lazy {
        DaggerCookCoreAppComponent.builder().build()
    }
}