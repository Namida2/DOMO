package com.example.featureOrder.presentation.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.menu.Dish
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.order.Order
import com.example.core.domain.entities.order.OrderItem
import com.example.core.domain.entities.tools.Event
import com.example.core.domain.entities.tools.NetworkConnectionListener
import com.example.core.domain.interfaces.OrdersService
import com.example.featureMenuDialog.domain.interfaces.OnDismissListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

typealias CurrentOrderChangeEvent = Event<List<OrderItem>>

sealed class OrderViewModelStates {
    object ShowingMenuDialog : OrderViewModelStates()
    object ShowingOrderMenuDialog : OrderViewModelStates()
    class ShowingDishMenuDialog(
        val orderItem: OrderItem,
        val dish: Dish
    ) : OrderViewModelStates()

    object Default : OrderViewModelStates()
}

//TODO: Subscribe on the order list changes and show a loader if the order screen was opened earlier than orders were loaded //STOPPED//
class OrderViewModel(
    private val ordersService: OrdersService
) : ViewModel(), OnDismissListener {

    private val _state: MutableLiveData<OrderViewModelStates> =
        MutableLiveData(OrderViewModelStates.Default)
    val state: LiveData<OrderViewModelStates> = _state
    private val _currentOrderChangedEvent: MutableLiveData<CurrentOrderChangeEvent> =
        MutableLiveData()
    val currentOrderChangedEvent: LiveData<CurrentOrderChangeEvent> = _currentOrderChangedEvent

    fun initCurrentOrder(tableId: Int, guestCount: Int) {
        ordersService.initCurrentOrder(tableId, guestCount)
        viewModelScope.launch {
            ordersService.subscribeOnCurrentOrderChanges().collect {
                _currentOrderChangedEvent.value = Event(it.toList())
            }
        }
    }

    fun onFbaClick() {
        if(state.value == OrderViewModelStates.ShowingMenuDialog) return
        _state.value = OrderViewModelStates.ShowingMenuDialog
    }

    fun getCurrentOrder(): Order =
        ordersService.currentOrder!!

    fun changeGuestsCount(newGuestsCount: Int) {
        ordersService.changeGuestsCount(newGuestsCount)
    }

    fun onNavigationIconClickListener() {
        _state.value = OrderViewModelStates.ShowingOrderMenuDialog
    }

    fun onOrderItemSelected(orderItem: OrderItem) {
        _state.value = OrderViewModelStates.ShowingDishMenuDialog(
            orderItem,
            MenuService.getDishById(orderItem.dishId) ?: return
        )
    }

    override fun onDismiss() {
        _state.value = OrderViewModelStates.Default
    }
}