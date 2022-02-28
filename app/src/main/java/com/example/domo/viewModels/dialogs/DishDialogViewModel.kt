package com.example.domo.viewModels.dialogs

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.menu.Dish
import com.example.core.domain.order.OrderItem
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.extensions.logD
import com.example.domo.R

sealed class DishDialogVMStates {
    open var errorMessage: com.example.core.domain.tools.ErrorMessage? = null

    object DishAlreadyAdded : DishDialogVMStates() {
        override var errorMessage: com.example.core.domain.tools.ErrorMessage? =
            com.example.core.domain.tools.ErrorMessage(
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

    var dish: Dish? = null
        set(value) {
            field = value
            dishesCount = 1
            commentary = ""
        }
    private var dishesCount = 1
    private var commentary = ""

    fun onAddButtonClick(view: View) {
        view.isActivated = false
        val tableId = ordersService.currentOrder?.orderId!!
        val resultOfAdding = ordersService.addOrderItem(
            OrderItem(dish!!.id, dishesCount, commentary)
        )
        if (!resultOfAdding) {
            _state.value = DishDialogVMStates.DishAlreadyAdded
            view.isActivated = true
        } else _state.value = DishDialogVMStates.DishSuccessfulAdded
        _state.value = DishDialogVMStates.Default
        logD(ordersService.currentOrder.toString())
    }

    fun onCountChanged(newCount: Int) {
        dishesCount = newCount
    }

    fun onCommentaryChanged(charSequence: CharSequence) {
        commentary = charSequence.toString()
    }
}