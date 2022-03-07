package com.example.featureCurrentOrders.presentation.currentOrders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.menu.Dish
import com.example.core.domain.order.Order
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.Event
import com.example.core.domain.tools.extensions.logD

typealias NewOrdersEvent = Event<List<Order>>

class CurrentOrdersViewModel(
    private val ordersService: OrdersService<OrdersServiceSub>
) : ViewModel() {

    private val _newOrdersEvent: MutableLiveData<NewOrdersEvent> = MutableLiveData()
    val newOrdersEvent: LiveData<NewOrdersEvent> = _newOrdersEvent

    private val ordersServiceSub = object : OrdersServiceSub {
        override fun invoke(orders: List<Order>) {
            _newOrdersEvent.value = Event(orders.toList())
        }
    }

    init {
        ordersService.subscribe(ordersServiceSub)
    }

    override fun onCleared() {
        super.onCleared()
        ordersService.unSubscribe(ordersServiceSub)
    }
}