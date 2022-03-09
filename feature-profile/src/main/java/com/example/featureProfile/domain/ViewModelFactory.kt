package com.example.featureProfile.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.tools.constants.OtherStringConstants.UNKNOWN_VIEW_MODEL_CLASS
import com.example.featureProfile.domain.di.ProfileDepsStore
import com.example.featureProfile.presentation.ProfileViewModel
import java.lang.IllegalArgumentException

object ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       val viewModel = when(modelClass) {
           ProfileViewModel::class.java -> ProfileViewModel(
               ProfileDepsStore.appComponent.provideLeaveAccountUseCase()
           )
           else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS)
       }
        return viewModel as T
    }

}