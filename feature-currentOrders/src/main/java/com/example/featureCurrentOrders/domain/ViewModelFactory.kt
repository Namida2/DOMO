package com.example.featureCurrentOrders.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.domain.di.DaggerCurrentOrdersAppComponent
import com.example.featureCurrentOrders.presentation.currentOrddersDetail.CurrentOrderDetailViewModel
import com.example.featureCurrentOrders.presentation.currentOrders.CurrentOrdersViewModel
import com.example.waiterCore.domain.tools.constants.OtherStringConstants.UNKNOWN_VIEW_MODEL_CLASS

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            CurrentOrdersViewModel::class.java -> {
                val currentOrdersViewModel = CurrentOrdersViewModel(
                    CurrentOrderDepsStore.deps.ordersService
                )
                //TODO: Move the appComponent to the DepsStore
                currentOrdersViewModel.also {
                    it.appComponent = DaggerCurrentOrdersAppComponent.builder()
                        .currentOrdersAppComponentDeps(
                            CurrentOrderDepsStore.deps
                        ).build()
                }
            }
            CurrentOrderDetailViewModel::class.java -> CurrentOrderDetailViewModel()
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS)
        }
        return viewModel as T
    }
}