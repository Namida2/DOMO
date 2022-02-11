package com.example.featureOrder.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.featureOrder.domain.di.OrderDepsStore
import com.example.featureOrder.presentation.order.OrderViewModel
import com.example.featureOrder.presentation.order.doalogs.menuDialog.MenuDialogViewModel
import com.example.featureOrder.presentation.order.doalogs.orderMenuDialog.OrderMenuDialogViewModel
import com.example.featureOrder.presentation.order.doalogs.dishDialog.DishDialogViewModel
import com.example.waiterCore.domain.menu.MenuService
import com.example.waiterCore.domain.tools.constants.OtherStringConstants.unknownViewModelClass

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            OrderViewModel::class.java -> OrderViewModel(
                OrderDepsStore.appComponent.provideOrderService()
            )
            MenuDialogViewModel::class.java -> MenuDialogViewModel(MenuService)
            OrderMenuDialogViewModel::class.java -> OrderMenuDialogViewModel(
                OrderDepsStore.appComponent.provideInsertOrderUseCase()
            )
            DishDialogViewModel::class.java -> DishDialogViewModel(
                OrderDepsStore.appComponent.provideOrderService()
            )
            else -> throw IllegalArgumentException(unknownViewModelClass + modelClass)
        }
        return viewModel as T
    }
}