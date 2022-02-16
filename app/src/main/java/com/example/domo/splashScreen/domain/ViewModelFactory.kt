package com.example.domo.splashScreen.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domo.authorization.presentation.LogInViewModel
import com.example.domo.registration.presentation.RegistrationViewModel
import com.example.domo.splashScreen.domain.di.SplashScreenAppComponent
import com.example.domo.splashScreen.presentation.SplashScreenViewModel
import com.example.waiterCore.domain.tools.constants.OtherStringConstants.unknownViewModelClass

object ViewModelFactory : ViewModelProvider.Factory {

    lateinit var appComponent: SplashScreenAppComponent
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val viewModel = when (modelClass) {
            SplashScreenViewModel::class.java -> SplashScreenViewModel(
                appComponent.provideReadMenuUseCase(),
                appComponent.provideReadOrdersUseCase(),
                appComponent.provideGetCurrentEmployeeUseCase()
            )
            LogInViewModel::class.java -> LogInViewModel(
                appComponent.provideLogInUseCase()
            )
            RegistrationViewModel::class.java -> RegistrationViewModel(
                appComponent.provideGetPostItemsUseCase(),
                appComponent.provideRegistrationUseCase()
            )
            else -> throw IllegalArgumentException(unknownViewModelClass + modelClass)
        }

        return viewModel as T
    }
}