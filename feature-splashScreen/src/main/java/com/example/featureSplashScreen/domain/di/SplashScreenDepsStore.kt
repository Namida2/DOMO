package com.example.featureSplashScreen.domain.di

object SplashScreenDepsStore {
    lateinit var deps: SplashScreenAppComponentDeps
    val appComponent: SplashScreenAppComponent by lazy {
        DaggerSplashScreenAppComponent.builder().provideDeps(deps).build()
    }
}
