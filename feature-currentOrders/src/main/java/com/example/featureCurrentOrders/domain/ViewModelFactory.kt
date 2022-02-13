package com.example.featureCurrentOrders.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.domain.di.DaggerCurrentOrdersAppComponent
import com.example.featureCurrentOrders.presentation.CurrentOrdersViewModel
import com.example.waiterCore.domain.tools.constants.OtherStringConstants.unknownViewModelClass

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            CurrentOrdersViewModel::class.java -> {
                val currentOrdersViewModel = CurrentOrdersViewModel(
                    CurrentOrderDepsStore.deps.ordersService
                )
                currentOrdersViewModel.also {
                    it.appComponent = DaggerCurrentOrdersAppComponent.builder()
                        .currentOrdersAppComponentDeps(
                            CurrentOrderDepsStore.deps
                        ).build()
                }
            }
            else -> throw IllegalArgumentException(unknownViewModelClass)
        }
        return viewModel as T
    }
}