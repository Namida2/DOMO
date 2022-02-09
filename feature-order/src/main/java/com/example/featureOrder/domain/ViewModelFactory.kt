package com.example.featureOrder.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.featureOrder.domain.di.OrderDepsStore
import com.example.featureOrder.presentation.order.OrderViewModel
import com.example.waiterCore.domain.tools.constants.OtherStringConstants.unknownViewModelClass

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            OrderViewModel::class.java -> OrderViewModel(
                OrderDepsStore.appComponent.provideOrderService()
            )
            else -> throw IllegalArgumentException(unknownViewModelClass + modelClass)
        }
        return viewModel as T
    }
}