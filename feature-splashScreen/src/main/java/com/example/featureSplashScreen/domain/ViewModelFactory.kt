package com.example.featureSplashScreen.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.tools.constants.OtherStringConstants.UNKNOWN_VIEW_MODEL_CLASS
import com.example.featureSplashScreen.domain.di.SplashScreenDepsStore
import com.example.featureSplashScreen.presentation.SplashScreenViewModel

object ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val appComponent = SplashScreenDepsStore.appComponent
        val viewModel = when (modelClass) {
            SplashScreenViewModel::class.java -> SplashScreenViewModel(
                appComponent.provideReadMenuUseCase(),
                appComponent.provideReadOrdersUseCase(),
                appComponent.provideGetCurrentEmployeeUseCase()
            )
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS + modelClass)
        }
        return viewModel as T
    }
}