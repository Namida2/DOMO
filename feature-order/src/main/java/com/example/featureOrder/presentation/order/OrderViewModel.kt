package com.example.featureOrder.presentation.order

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.interfaces.OrdersService
import com.example.core.domain.menu.Dish
import com.example.core.domain.menu.MenuService
import com.example.core.domain.order.Order
import com.example.core.domain.order.OrderItem
import com.example.core.domain.order.OrdersServiceSub
import com.example.core.domain.tools.Event
import com.example.core.domain.tools.constants.OtherStringConstants.ORDER_ITEM_NOT_FOUNT
import com.example.core.domain.tools.extensions.logD
import com.example.featureMenuDialog.domain.interfaces.OnDismissListener
import com.example.featureMenuDialog.presentation.menuDialog.MenuDialogStates
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

typealias CurrentOrderChangeEvent = Event<List<OrderItem>>

sealed class OrderViewModelStates {
    class ShowingMenuDialog(val fba: View) : OrderViewModelStates()
    object ShowingOrderMenuDialog : OrderViewModelStates()
    class ShowingDishMenuDialog(
        val orderItem: OrderItem,
        val dish: Dish
    ) : OrderViewModelStates()

    object Default : OrderViewModelStates()
}

//TODO: Do this things
sealed class OrderItemsRecyclerViewStates {
    object NoScrolled : OrderItemsRecyclerViewStates()
    object ScrolledDownwards : OrderItemsRecyclerViewStates()
    object ScrolledUpwards : OrderItemsRecyclerViewStates()
}

//TODO: Subscribe on the order list changes and show a loader if the order screen was opened earlier than orders were loaded //STOPPED//
class OrderViewModel(
    private val ordersService: OrdersService<OrdersServiceSub>
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

    fun onFbaClick(fba: View) {
        _state.value = OrderViewModelStates.ShowingMenuDialog(fba)
    }

    fun getCurrentOrder(): Order =
        ordersService.currentOrder!!

    fun changeGuestsCount(newGuestsCount: Int) {
        ordersService.changeGuestsCount(newGuestsCount)
    }

    fun onNavigationIconClickListener() {
        _state.value = OrderViewModelStates.ShowingOrderMenuDialog
    }

    fun onOrderItemSelected(orderItemId: String) {
        val orderItem = ordersService.currentOrder!!.orderItems.find {
            it.getOrderIemId() == orderItemId
        } ?: throw IllegalArgumentException(ORDER_ITEM_NOT_FOUNT + orderItemId)
        _state.value = OrderViewModelStates.ShowingDishMenuDialog(
            orderItem,
            MenuService.getDishById(orderItem.dishId)
        )
    }

    override fun onDismiss() {
        _state.value = OrderViewModelStates.Default
    }
}