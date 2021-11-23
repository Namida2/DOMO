package com.example.domo.viewModels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import entities.interfaces.OnDismissListener

sealed class SharedViewModelStates {
    class ShowingMenuDialog (val fba: View): SharedViewModelStates()
    object Default: SharedViewModelStates()
}

class WaiterActivityOrderFragmentSharedViewModel(
    //private val ordersService: OrdersService
): ViewModel(), OnDismissListener {
    private val _states: MutableLiveData<SharedViewModelStates> = MutableLiveData(SharedViewModelStates.Default)
    val states: LiveData<SharedViewModelStates> = _states

    fun onFbaClick(fba: View) {
        _states.value = SharedViewModelStates.ShowingMenuDialog(fba)
    }

    override fun onDismiss() {
        _states.value = SharedViewModelStates.Default
    }
}