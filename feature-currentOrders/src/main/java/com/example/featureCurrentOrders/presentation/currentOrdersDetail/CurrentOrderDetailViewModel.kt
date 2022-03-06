package com.example.featureCurrentOrders.presentation.currentOrdersDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.Order
import com.example.core.domain.order.OrderItem
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.Event
import kotlin.properties.Delegates

typealias DishesExistEvent = Event<List<OrderItem>>

class CurrentOrderDetailViewModel(
    private val ordersService: OrdersService<OrdersServiceSub>
) : ViewModel() {

    var orderId: Int? = null
    set(value) {
        field = value
        ordersService.subscribe(subscriber)
    }
    private val _newOrderItemsEvent = MutableLiveData<DishesExistEvent>()
    val newOrderItemsEvent: LiveData<DishesExistEvent> = _newOrderItemsEvent

    private val subscriber = object : OrdersServiceSub {
        override fun invoke(orders: List<Order>) {
            orders.find { it.orderId == orderId}?.let {
                _newOrderItemsEvent.value = Event(it.orderItems.toList())
            }
        }
    }

    override fun onCleared() {
        ordersService.unSubscribe(subscriber)
        super.onCleared()
    }
}