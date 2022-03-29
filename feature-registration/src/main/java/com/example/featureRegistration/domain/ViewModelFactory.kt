package com.example.featureRegistration.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.entities.tools.constants.OtherStringConstants.UNKNOWN_VIEW_MODEL_CLASS
import com.example.featureRegistration.domain.di.RegistrationDepsStore
import com.example.featureRegistration.presentation.RegistrationViewModel

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            RegistrationViewModel::class.java -> RegistrationViewModel(
                RegistrationDepsStore.appComponent.provideGetPostItemUseCase(),
                RegistrationDepsStore.appComponent.provideRegistrationUseCase()
            )
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS)
        }
        return viewModel as T
    }
}