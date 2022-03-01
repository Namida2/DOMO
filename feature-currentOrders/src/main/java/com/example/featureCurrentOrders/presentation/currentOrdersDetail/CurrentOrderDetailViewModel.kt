package com.example.featureCurrentOrders.presentation.currentOrdersDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.order.OrderItem
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.Event

typealias DishesExistEvent = Event<List<OrderItem>>

class CurrentOrderDetailViewModel(
    private val ordersService: OrdersService<OrdersServiceSub>
) : ViewModel() {

    private val _dishesExitEvent = MutableLiveData<DishesExistEvent>()
    val dishesExitEvent: LiveData<DishesExistEvent> = _dishesExitEvent

    fun getDishesByOrderId(orderId: Int) {
        when (val order = ordersService.getOrderById(orderId)) {
            null -> {
                //TODO: Subscribe to newOrderService updates
            }
            else -> _dishesExitEvent.value =
                Event(order.orderItems.toList())
        }
    }
}