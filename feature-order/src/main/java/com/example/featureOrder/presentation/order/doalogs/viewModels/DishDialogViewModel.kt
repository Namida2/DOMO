package com.example.featureOrder.presentation.order.doalogs.viewModels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.featureOrder.R
import com.example.waiterCore.domain.menu.Dish
import com.example.waiterCore.domain.tools.ErrorMessage

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
//    private val ordersService: OrderServiceInterface<OrderServiceSub>,
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
//        view.isActivated = false
//        val tableId = ordersService.currentOrder?.tableId!!
//        val resultOfAdding = ordersService.addOrderItem (
//            OrderItem(tableId, dish!!.id, dishesCount, commentary)
//        )
//        if (!resultOfAdding) {
//            _state.value = DishDialogVMStates.DishAlreadyAdded
//            view.isActivated = true
//        }
//        else _state.value = DishDialogVMStates.DishSuccessfulAdded
//        _state.value = DishDialogVMStates.Default
//        logD(ordersService.currentOrder.toString())
    }

    fun onCountChanged(newCount: Int) {
        dishesCount = newCount
    }

    fun onCommentaryChanged(charSequence: CharSequence) {
        commentary = charSequence.toString()
    }
}