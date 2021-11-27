package com.example.domo.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domo.viewModels.activities.SplashScreenViewModel
import com.example.domo.viewModels.fragments.TablesViewModel
import com.google.common.collect.Tables
import di.AppComponent
import java.lang.IllegalArgumentException

class ViewModelFactory (private val appComponent: AppComponent): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            SplashScreenViewModel::class.java -> SplashScreenViewModel(appComponent.provideSplashScreenModel())
            LogInViewModel::class.java -> LogInViewModel(appComponent.provideLogInModel())
            RegistrationViewModel::class.java -> RegistrationViewModel(appComponent.provideRegistrationModel())
            WaiterActivityOrderFragmentSharedViewModel::class.java -> WaiterActivityOrderFragmentSharedViewModel()
            MenuDialogViewModel::class.java -> MenuDialogViewModel(appComponent.provideMenuDialogModel())
            TablesViewModel::class.java -> TablesViewModel()
            else -> throw IllegalArgumentException("Unknown viewModel class")
        }
        return viewModel as T
    }


}