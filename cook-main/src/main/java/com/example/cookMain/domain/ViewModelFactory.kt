package com.example.cookMain.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cookMain.domain.di.CookMainDepsStore.appComponent
import com.example.cookMain.presentation.CookMainViewModel
import com.example.core.domain.entities.tools.constants.OtherStringConstants.UNKNOWN_VIEW_MODEL_CLASS
import java.lang.IllegalArgumentException

object ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            CookMainViewModel::class.java -> CookMainViewModel (
                appComponent.provideMenVersionListener()
                    )
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS)
        }
        return viewModel as T
    }

}