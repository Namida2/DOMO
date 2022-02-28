package com.example.domo.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domo.viewModels.activities.SplashScreenViewModel
import com.example.domo.viewModels.dialogs.DishDialogViewModel
import com.example.domo.viewModels.dialogs.MenuDialogViewModel
import com.example.domo.viewModels.dialogs.OrderMenuDialogViewModel
import com.example.domo.viewModels.fragments.TablesViewModel
import com.example.domo.viewModels.shared.WaiterActOrderFragSharedViewModel
import com.example.core.domain.tools.constants.OtherStringConstants.UNKNOWN_VIEW_MODEL_CLASS
import di.AppComponent

object ViewModelFactory : ViewModelProvider.Factory {
    lateinit var appComponent: AppComponent
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            SplashScreenViewModel::class.java -> SplashScreenViewModel(appComponent.provideSplashScreenModel())
            LogInViewModel::class.java -> LogInViewModel(appComponent.provideLogInModel())
            RegistrationViewModel::class.java -> RegistrationViewModel(appComponent.provideRegistrationModel())
            WaiterActOrderFragSharedViewModel::class.java -> WaiterActOrderFragSharedViewModel(
                appComponent.provideWaiterActOrderFragModel())
            MenuDialogViewModel::class.java -> MenuDialogViewModel(appComponent.provideMenuDialogModel())
            TablesViewModel::class.java -> TablesViewModel()
            DishDialogViewModel::class.java -> DishDialogViewModel(appComponent.provideOrderService())
            OrderMenuDialogViewModel::class.java -> OrderMenuDialogViewModel(appComponent.provideOrderMenuDialogViewModel())
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS + modelClass)
        }
        return viewModel as T
    }
}