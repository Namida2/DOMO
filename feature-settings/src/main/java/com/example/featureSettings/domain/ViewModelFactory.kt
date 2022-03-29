package com.example.featureSettings.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.entities.tools.constants.OtherStringConstants.UNKNOWN_VIEW_MODEL_CLASS
import com.example.featureSettings.domain.di.SettingsDepsStore.appComponent
import com.example.featureSettings.presentation.SettingsViewModel
import java.lang.IllegalArgumentException

object ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            SettingsViewModel::class.java -> SettingsViewModel(
                appComponent.provideSaveMenuUseCase()
            )
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS)
        }
        return viewModel as T
    }
}