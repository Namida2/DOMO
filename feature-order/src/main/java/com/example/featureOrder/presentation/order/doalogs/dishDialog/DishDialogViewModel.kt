package com.example.featureOrder.presentation.order.doalogs.dishDialog

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.featureOrder.R
import com.example.waiterCore.domain.interfaces.OrdersService
import com.example.waiterCore.domain.menu.Dish
import com.example.waiterCore.domain.order.OrderItem
import com.example.waiterCore.domain.order.OrdersServiceSub
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.waiterCore.domain.tools.extensions.logD

sealed class DishDialogVMStates {
    open var errorMessage: ErrorMessage? = null

    object DishAlreadyAdded : DishDialogVMStates() {
        override var errorMessage: ErrorMessage? = ErrorMessage(
            R.string.dishAlreadyAddedTitle,
            R.string.dishAlreadyAddedMessage
        )
    }

    object DishSuccessfulAdded : DishDialogVMStates()
    object Default : DishDialogVMStates()
}

class DishDialogViewModel(
    private val ordersService: OrdersService<OrdersServiceSub>,
) : ViewModel() {

    private var _state = MutableLiveData<DishDialogVMStates>(DishDialogVMStates.Default)
    val state: LiveData<DishDialogVMStates> = _state

    var orderItem: Dish? = null

    fun onAddButtonClick(view: View, dishesCount: String, commentary: String) {
        view.isActivated = false
        val resultOfAdding = ordersService.addOrderItem (
           OrderItem(orderItem!!.id, dishesCount.toInt(), commentary)
        )
        if (!resultOfAdding) {
            _state.value = DishDialogVMStates.DishAlreadyAdded
            view.isActivated = true
        }
        else _state.value = DishDialogVMStates.DishSuccessfulAdded
        _state.value = DishDialogVMStates.Default
        logD(ordersService.currentOrder.toString())
    }
}