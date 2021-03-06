package com.example.featureLogIn.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.entities.tools.constants.OtherStringConstants.UNKNOWN_VIEW_MODEL_CLASS
import com.example.featureLogIn.domain.di.LogInDepsStore
import com.example.featureLogIn.domain.di.LogInDepsStore.appComponent
import com.example.featureLogIn.presentation.LogInViewModel

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            LogInViewModel::class.java -> LogInViewModel(
                appComponent.provideLogInUseCase(),
                appComponent.provideReadAdminPasswordUseCase(),
                appComponent.provideLeaveAccountUseCase()
            )
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS)
        }
        return viewModel as T
    }
}