package com.example.featureCurrentOrders.presentation.currentOrdersDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.order.OrderItem
import com.example.core.domain.entities.tools.Event
import com.example.core.domain.interfaces.OrdersService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

typealias DishesExistEvent = Event<List<OrderItem>>

class CurrentOrderDetailViewModel(
    private val ordersService: OrdersService
) : ViewModel() {

    private val _newOrderItemsEvent = MutableLiveData<DishesExistEvent>()
    val newOrderItemsEvent: LiveData<DishesExistEvent> = _newOrderItemsEvent

    var orderId: Int? = null
        set(value) {
            field = value
            viewModelScope.launch {
                ordersService.subscribeOnOrdersChanges().collect { orders ->
                    orders.find { it.orderId == orderId }?.let {
                        _newOrderItemsEvent.value = Event(it.orderItems.toList())
                    }
                }
            }
        }
}