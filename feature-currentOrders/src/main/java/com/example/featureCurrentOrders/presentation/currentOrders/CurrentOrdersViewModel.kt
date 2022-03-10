package com.example.featureCurrentOrders.presentation.currentOrders

import androidx.lifecycle.*
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.menu.Dish
import com.example.core.domain.order.Order
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.Event
import com.example.core.domain.tools.extensions.logD
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

typealias NewOrdersEvent = Event<List<Order>>

class CurrentOrdersViewModel(
    private val ordersService: OrdersService<OrdersServiceSub>
) : ViewModel() {

    private val _newOrdersEvent: MutableLiveData<NewOrdersEvent> = MutableLiveData()
    val newOrdersEvent: LiveData<NewOrdersEvent> = _newOrdersEvent

    init {
        viewModelScope.launch {
            ordersService.subscribeOnOrdersChanges().collect { orders ->
                _newOrdersEvent.value = Event(orders.toList())
            }
        }
    }

}