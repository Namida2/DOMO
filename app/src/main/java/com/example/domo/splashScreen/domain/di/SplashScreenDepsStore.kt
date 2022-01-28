package com.example.domo.splashScreen.domain.di

import com.example.domo.splashScreen.domain.ViewModelFactory

object SplashScreenDepsStore {
    var deps: SplashScreenAppComponentDeps? = null
//    set(value) {
//        if(value == null) return
//        if(deps != null) return
//        field = value
//        ViewModelFactory.appComponent = DaggerSplashScreenAppComponent.builder().putDeps(value).build()
//    }
}
