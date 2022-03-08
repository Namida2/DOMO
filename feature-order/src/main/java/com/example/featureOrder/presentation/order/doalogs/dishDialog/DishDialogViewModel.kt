package com.example.featureOrder.presentation.order.doalogs.dishDialog

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.menu.Dish
import com.example.core.domain.order.OrderItem
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.enims.AddingDishMods
import com.example.core.domain.tools.extensions.logD
import com.example.featureOrder.R

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

    var orderItem: Dish? = null
    var addingDishMode: AddingDishMods? = null
    var aldCommentary: String? = null
    private var _state = MutableLiveData<DishDialogVMStates>(DishDialogVMStates.Default)

    val state: LiveData<DishDialogVMStates> = _state

    fun onAddButtonClick(view: View, dishesCount: String, commentary: String) {
        view.isActivated = false
        var result = false
        when(addingDishMode) {
            AddingDishMods.INSERTING -> {
                result = ordersService.addOrderItem(
                    OrderItem(orderItem!!.id, dishesCount.toInt(), commentary),
                )
            }
            AddingDishMods.UPDATING -> {
                result = ordersService.updateOrderItem(
                    OrderItem(orderItem!!.id, dishesCount.toInt(), commentary),
                    aldCommentary!!
                )
            }
            else -> {logD("$this: addingDishMode is null")}
        }
        if (!result) {
            _state.value = DishDialogVMStates.DishAlreadyAdded
            view.isActivated = true
        } else _state.value = DishDialogVMStates.DishSuccessfulAdded
        _state.value = DishDialogVMStates.Default
        logD(ordersService.currentOrder.toString())
    }
}