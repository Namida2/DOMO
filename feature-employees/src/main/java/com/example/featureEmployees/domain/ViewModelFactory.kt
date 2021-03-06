package com.example.featureEmployees.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.entities.tools.constants.OtherStringConstants.UNKNOWN_VIEW_MODEL_CLASS
import com.example.featureEmployees.domain.di.EmployeesDepsStore.appComponent
import com.example.featureEmployees.presentation.dialogs.EmployeeDetailViewModel
import com.example.featureEmployees.presentation.fragments.EmployeesViewModel

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            EmployeesViewModel::class.java -> EmployeesViewModel(
                appComponent.provideReadEmployeesUseCase(),
                appComponent.provideEmployeesService(),
            )
            EmployeeDetailViewModel::class.java -> EmployeeDetailViewModel(
                appComponent.provideSetPermissionUseCase(),
                appComponent.provideDeleteEmployeeUseCase()
            )
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS)
        }
        return viewModel as T
    }
}