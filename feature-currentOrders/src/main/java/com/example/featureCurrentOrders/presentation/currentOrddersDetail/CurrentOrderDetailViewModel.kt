package com.example.featureCurrentOrders.presentation.currentOrddersDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.waiterCore.domain.menu.Dish
import com.example.waiterCore.domain.tools.Event

class CurrentOrderDetailViewModel: ViewModel() {

    private val _dishesExitEvent = MutableLiveData<Event<Dish>>()
    val dishesExitEvent = MutableLiveData<Event<Dish>>()

    fun getDishesByOrderId() {

    }
}