package com.example.domo.viewModels.dialogs

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.models.OrdersService
import entities.menu.Dish
import entities.tools.Event

class DishDialogViewModel(
    private val ordersService: OrdersService
): ViewModel() {

    private var _addDishEvent = MutableLiveData<Event<Unit>>()
    val addDishEvent: LiveData<Event<Unit>> = _addDishEvent

    var dish: Dish? = null
    
    fun onAddButtonClick(view: View) {
        view.isActivated = false
        ordersService.
    }
}