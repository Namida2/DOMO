package com.example.featureCurrentOrders.presentation.currentOrders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.order.Order
import com.example.core.domain.entities.tools.Event
import com.example.core.domain.entities.tools.NetworkConnectionListener
import com.example.core.domain.interfaces.OrdersService
import com.example.featureCurrentOrders.domain.OrderInfo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

typealias NewOrdersEvent = Event<List<Order>>
typealias OnOrderSelectedEvent = Event<OrderInfo>

class CurrentOrdersViewModel(
    private val ordersService: OrdersService
) : ViewModel() {

    private val _newOrdersEvent: MutableLiveData<NewOrdersEvent> = MutableLiveData()
    val newOrdersEvent: LiveData<NewOrdersEvent> = _newOrdersEvent
    private val _onOrderSelectedEvent: MutableLiveData<OnOrderSelectedEvent> = MutableLiveData()
    val onOrderSelectedEvent: LiveData<OnOrderSelectedEvent> = _onOrderSelectedEvent

    init {
        viewModelScope.launch {
            ordersService.subscribeOnOrdersChanges().collect { orders ->
                _newOrdersEvent.value = Event(orders.toList())
            }
        }
    }

    fun onOrderClick(order: Order) {
        _onOrderSelectedEvent.value = Event(
            OrderInfo(order, order.isCompleted())
        )
    }

}