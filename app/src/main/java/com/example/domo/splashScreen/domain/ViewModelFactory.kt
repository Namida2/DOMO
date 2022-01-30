package com.example.domo.splashScreen.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domo.authorization.presentation.LogInViewModel
import com.example.domo.splashScreen.domain.di.SplashScreenAppComponent
import com.example.domo.splashScreen.presentation.SplashScreenViewModel
import java.lang.IllegalArgumentException

object ViewModelFactory: ViewModelProvider.Factory {

    private const val illegalArgumentExceptionMessage = "Unknown viewModel class: "
    lateinit var appComponent: SplashScreenAppComponent
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val viewModel = when(modelClass) {
            SplashScreenViewModel::class.java -> SplashScreenViewModel(
                appComponent.provideReadMenuUseCase(),
                appComponent.provideGetCurrentEmployeeUseCase()
            )
            LogInViewModel::class.java -> LogInViewModel(
                appComponent.provideLogInUseCase()
            )
            else -> throw IllegalArgumentException(illegalArgumentExceptionMessage + modelClass)
        }

        return viewModel as T
    }
}