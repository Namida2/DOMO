package com.example.domo.viewModels.shared

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.models.interfaces.WaiterActOrderFragSharedViewModelInterface
import entities.interfaces.OnDismissListener
import entities.order.OrderItem

sealed class SharedViewModelStates {
    class ShowingMenuDialog (val fba: View): SharedViewModelStates()
    object Default: SharedViewModelStates()
}

class WaiterActOrderFragSharedViewModel (
    private val model: WaiterActOrderFragSharedViewModelInterface
): ViewModel(), OnDismissListener {
    private val _states: MutableLiveData<SharedViewModelStates> = MutableLiveData(
        SharedViewModelStates.Default)
    val states: LiveData<SharedViewModelStates> = _states

    fun onFbaClick(fba: View) {
        _states.value = SharedViewModelStates.ShowingMenuDialog(fba)
    }

    fun initCurrentOrder(tableId: Int, guestCount: Int) {
        model.initCurrentOrder(tableId, guestCount)
    }
    fun getCurrentOrderItems(): Set<OrderItem> = model.getCurrentOrderItems()

    //TODO: Add the OrderMenuBottomSheetDialog and confirming of the current order // STOPPED //
    fun onNavigationIconClickListener(view: View) {

    }

    override fun onDismiss() {
        _states.value = SharedViewModelStates.Default
    }
}