package com.example.domo.viewModels.dialogs

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.domo.models.OrdersService

class DishDialogViewModel(
    private val ordersService: OrdersService
): ViewModel() {
    fun onAddButtonClick(view: View) {
        view.isActivated = false
    }
}