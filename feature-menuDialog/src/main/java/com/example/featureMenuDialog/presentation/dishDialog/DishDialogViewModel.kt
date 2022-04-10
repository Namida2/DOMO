package com.example.featureMenuDialog.presentation.dishDialog

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.entities.menu.Dish
import com.example.core.domain.entities.order.OrderItem
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.constants.Messages.dishAlreadyAddedMessage
import com.example.core.domain.entities.tools.constants.Messages.dishNotFoundMessage
import com.example.core.domain.entities.tools.enums.AddingDishMods
import com.example.core.domain.entities.tools.extensions.isEmptyField
import com.example.core.domain.entities.tools.extensions.logD

sealed class DishDialogVMStates {
    class OnFailure(
        val errorMessage: ErrorMessage = dishAlreadyAddedMessage
    ): DishDialogVMStates()
    object OnSuccess : DishDialogVMStates()
    object Default : DishDialogVMStates()
}

class DishDialogViewModel(
    private val ordersService: OrdersService,
) : ViewModel() {

    var dish: Dish? = null
    var addingDishMode: AddingDishMods? = null
    var aldCommentary: String? = null
    private var _state = MutableLiveData<DishDialogVMStates>(DishDialogVMStates.Default)
    private var minDishesCount = 1

    val state: LiveData<DishDialogVMStates> = _state

    fun onAddButtonClick(view: View, dishesCount: String, commentary: String) {
        view.isActivated = false
        var dishesCountInt = minDishesCount
        if(!isEmptyField(dishesCount)) {
            dishesCountInt = dishesCount.toIntOrNull().run {
                if(this == 0 || this == null) minDishesCount else this
            }
        }
        val result: Boolean = when (addingDishMode) {
            AddingDishMods.INSERTING -> {
                ordersService.addOrderItem(
                    OrderItem(dish!!.id, dishesCountInt, commentary),
                )
            }
            AddingDishMods.UPDATING -> {
                ordersService.updateOrderItem(
                    OrderItem(dish!!.id, dishesCountInt, commentary),
                    aldCommentary!!
                )
            }
            else -> { logD("$this: addingDishMode is null"); false}
        }
        if (result) {
            _state.value = DishDialogVMStates.OnSuccess
        } else  _state.value = DishDialogVMStates.OnFailure()
        _state.value = DishDialogVMStates.Default
        view.isActivated = true
    }

    fun onRemoveButtonClick(view: View, commentary: String) {
        view.isActivated = false
        val result = ordersService.deleteFromCurrentOrder(dish!!, commentary)
        if (result)
            _state.value = DishDialogVMStates.OnSuccess
        else _state.value = DishDialogVMStates.OnFailure(errorMessage = dishNotFoundMessage)
        _state.value = DishDialogVMStates.Default
        view.isActivated = true
    }
}