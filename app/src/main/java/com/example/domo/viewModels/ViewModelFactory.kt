package com.example.domo.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import di.AppComponent
import java.lang.IllegalArgumentException

class ViewModelFactory (var appComponent: AppComponent): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            SplashScreenViewModel::class.java -> SplashScreenViewModel(appComponent.provideSplashScreenModel())
            else -> throw IllegalArgumentException("Unknown viewModel class")
        }
        return viewModel as T
    }


}