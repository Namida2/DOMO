package com.example.featureCurrentOrders.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cookCore.domain.di.CookCoreAppComponentStore
import com.example.cookCore.presentation.CookCurrentOrderDetailViewModel
import com.example.featureCurrentOrders.domain.di.CurrentOrderDepsStore
import com.example.featureCurrentOrders.presentation.currentOrdersDetail.CurrentOrderDetailViewModel
import com.example.featureCurrentOrders.presentation.currentOrders.CurrentOrdersViewModel
import com.example.core.domain.entities.tools.constants.OtherStringConstants.UNKNOWN_VIEW_MODEL_CLASS
import com.example.featureCurrentOrders.presentation.completedOrderMenu.CompletedOrderMenuDialog
import com.example.featureCurrentOrders.presentation.completedOrderMenu.CompletedOrderViewModel

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
            CookCurrentOrderDetailViewModel::class.java -> CookCurrentOrderDetailViewModel(
                CookCoreAppComponentStore.cookCoreAppComponent.provideChangeOrderItemStateUseCase()
            )
            CompletedOrderViewModel::class.java -> CompletedOrderViewModel(
                CurrentOrderDepsStore.appComponent.provideDeleteOrderUseCase()
            )
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS)
        }
        return viewModel as T
    }
}