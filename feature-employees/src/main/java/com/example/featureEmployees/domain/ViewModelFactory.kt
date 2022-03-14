package com.example.featureEmployees.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.tools.constants.OtherStringConstants.UNKNOWN_VIEW_MODEL_CLASS
import com.example.featureEmployees.domain.di.EmployeesDepsStore
import com.example.featureEmployees.presentation.EmployeesViewModel
import java.lang.IllegalArgumentException

object ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            EmployeesViewModel::class.java -> EmployeesViewModel(
                EmployeesDepsStore.appComponent.provideReadEmployeesUseCase()
            )
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS)
        }

        return viewModel as T
    }
}