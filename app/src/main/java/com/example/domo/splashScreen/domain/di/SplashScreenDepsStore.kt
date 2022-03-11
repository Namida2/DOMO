package com.example.domo.splashScreen.domain.di

import com.example.domo.splashScreen.domain.ViewModelFactory


object SplashScreenDepsStore {
    lateinit var deps: SplashScreenAppComponentDeps
    val appComponent: SplashScreenAppComponent by lazy {
        val component = DaggerSplashScreenAppComponent.builder().provideDeps(deps).build()
        ViewModelFactory.appComponent = component
        component
    }
}
