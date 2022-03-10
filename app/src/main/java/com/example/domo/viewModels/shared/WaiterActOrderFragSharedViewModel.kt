package com.example.domo.viewModels.shared

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.order.CurrentOrderServiceSub
import com.example.core.domain.order.Order
import com.example.core.domain.order.OrderItem
import com.example.domo.models.interfaces.WaiterActOrderFragSharedViewModel
import entities.interfaces.OnDismissListener

typealias CurrentOrderChangeEvent = com.example.core.domain.tools.Event<List<OrderItem>>

sealed class SharedViewModelStates {
    class ShowingMenuDialog(val fba: View) : SharedViewModelStates()
    object ShowingOrderMenuDialog : SharedViewModelStates()
    object Default : SharedViewModelStates()
}

//TODO: Do this things
sealed class OrderItemsRecyclerViewStates {
    object NoScrolled : OrderItemsRecyclerViewStates()
    object ScrolledDownwards : OrderItemsRecyclerViewStates()
    object ScrolledUpwards : OrderItemsRecyclerViewStates()
}

class WaiterActOrderFragSharedViewModel(
    private val model: WaiterActOrderFragSharedViewModel,
) : ViewModel(), OnDismissListener {

    private val _states: MutableLiveData<SharedViewModelStates> = MutableLiveData(
        SharedViewModelStates.Default
    )
    val states: LiveData<SharedViewModelStates> = _states

    private val _currentOrderChangedEvent: MutableLiveData<CurrentOrderChangeEvent> =
        MutableLiveData()
    val currentOrderChangedEvent: LiveData<CurrentOrderChangeEvent> = _currentOrderChangedEvent

    private val currentOrderSubscriber: CurrentOrderServiceSub = {
        _currentOrderChangedEvent.value = com.example.core.domain.tools.Event(it)
    }

    fun onFbaClick(fba: View) {
        _states.value = SharedViewModelStates.ShowingMenuDialog(fba)
    }

    fun initCurrentOrder(tableId: Int, guestCount: Int) {
//        model.initCurrentOrder(tableId, guestCount)
//        model.subscribeToCurrentOrderChangers(currentOrderSubscriber)
    }

    fun getCurrentOrder(): Order =
        model.currentOrder!!

    fun changeGuestsCount(newGuestsCount: Int) {
        model.changeGuestsCount(newGuestsCount)
    }

    //TODO: Add the OrderMenuBottomSheetDialog and confirming of the current order
    fun onNavigationIconClickListener() {
        _states.value = SharedViewModelStates.ShowingOrderMenuDialog
    }

    override fun onDismiss() {
        _states.value = SharedViewModelStates.Default
    }

    override fun onCleared() {
//        super.onCleared()
//        model.unSubscribeToCurrentOrderChangers(currentOrderSubscriber)
    }
}