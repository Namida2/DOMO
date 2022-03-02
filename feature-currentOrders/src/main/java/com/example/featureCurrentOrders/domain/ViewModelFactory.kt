package com.example.featureCurrentOrders.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cookCore.presentation.CookCurrentOrderDetailViewModel
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.presentation.currentOrdersDetail.CurrentOrderDetailViewModel
import com.example.featureCurrentOrders.presentation.currentOrders.CurrentOrdersViewModel
import com.example.core.domain.tools.constants.OtherStringConstants.UNKNOWN_VIEW_MODEL_CLASS

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            CurrentOrdersViewModel::class.java ->
                CurrentOrdersViewModel(
                    CurrentOrderDepsStore.deps.ordersService
                )
            CurrentOrderDetailViewModel::class.java -> CurrentOrderDetailViewModel(
                CurrentOrderDepsStore.deps.ordersService
            )
            //TODO: Provide the dependencies //STOPPED//
            CookCurrentOrderDetailViewModel::class.java -> CookCurrentOrderDetailViewModel()
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS)
        }
        return viewModel as T
    }
}