package com.example.waiterMain.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.entities.tools.constants.OtherStringConstants.UNKNOWN_VIEW_MODEL_CLASS
import com.example.waiterMain.domain.di.WaiterMainDepsStore.appComponent
import com.example.waiterMain.presentation.WaiterMainViewModel

object ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            WaiterMainViewModel::class.java -> WaiterMainViewModel(
                appComponent.provideMenuVersionListener()
            )
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS)
        }
        return viewModel as T
    }
}