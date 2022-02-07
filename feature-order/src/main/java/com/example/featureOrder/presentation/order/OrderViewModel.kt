package com.example.featureOrder.presentation.order

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.waiterCore.domain.order.Order
import com.example.waiterCore.domain.order.OrderItem
import com.example.waiterCore.domain.tools.Event

typealias CurrentOrderChangeEvent = Event<List<OrderItem>>

sealed class OrderViewModelStates {
    class ShowingMenuDialog(val fba: View) : OrderViewModelStates()
    object ShowingOrderMenuDialog : OrderViewModelStates()
    object Default : OrderViewModelStates()
}
//TODO: Do this things
sealed class OrderItemsRecyclerViewStates {
    object NoScrolled : OrderItemsRecyclerViewStates()
    object ScrolledDownwards : OrderItemsRecyclerViewStates()
    object ScrolledUpwards: OrderItemsRecyclerViewStates()
}

class OrderViewModel(
//    private val model: WaiterActOrderFragSharedViewModelInterface,
) : ViewModel() //, OnDismissListener
{

    private val _states: MutableLiveData<OrderViewModelStates> = MutableLiveData(
        OrderViewModelStates.Default)
    val states: LiveData<OrderViewModelStates> = _states

    private val _currentOrderChangedEvent: MutableLiveData<CurrentOrderChangeEvent> =
        MutableLiveData()
    val currentOrderChangedEvent: LiveData<CurrentOrderChangeEvent> = _currentOrderChangedEvent
//
//    private val currentOrderSubscriber: CurrentOrderServiceSub = {
//        _currentOrderChangedEvent.value = Event(it)
//    }
//
//    fun onFbaClick(fba: View) {
//        _states.value = OrderViewModelStates.ShowingMenuDialog(fba)
//    }
//
//    fun initCurrentOrder(tableId: Int, guestCount: Int) {
//        model.initCurrentOrder(tableId, guestCount)
//        model.subscribeToCurrentOrderChangers(currentOrderSubscriber)
//    }
//
//    fun getCurrentOrder(): Order =
//        model.currentOrder!!
//
//    fun changeGuestsCount(newGuestsCount: Int) {
//        model.changeGuestsCount(newGuestsCount)
//    }
//
//    //TODO: Add the OrderMenuBottomSheetDialog and confirming of the current order
//    fun onNavigationIconClickListener() {
//        _states.value = OrderViewModelStates.ShowingOrderMenuDialog
//    }
//
//    override fun onDismiss() {
//        _states.value = OrderViewModelStates.Default
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        model.unSubscribeToCurrentOrderChangers(currentOrderSubscriber)
//    }
}