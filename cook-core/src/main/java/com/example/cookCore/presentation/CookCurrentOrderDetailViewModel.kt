package com.example.cookCore.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookCore.domain.useCases.ChangeOrderItemStateUseCase
import com.example.core.domain.entities.order.OrderItem
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.Event
import com.example.core.domain.entities.tools.NetworkConnectionListener
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.entities.tools.SimpleTask
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class CookCurrentOrderDetailVMStates {
    object UpdatingData : CookCurrentOrderDetailVMStates()
    object OnUpdatingSuccess : CookCurrentOrderDetailVMStates()
    class OnUpdatingFailure(val errorMessage: ErrorMessage) : CookCurrentOrderDetailVMStates()
    object Default : CookCurrentOrderDetailVMStates()
}

class CookCurrentOrderDetailViewModel(
    private val changeOrderItemStateUseCase: ChangeOrderItemStateUseCase
) : ViewModel() {

    private val _state = MutableLiveData<CookCurrentOrderDetailVMStates>()
    val state: LiveData<CookCurrentOrderDetailVMStates> = _state

    fun changeOrderItemStatus(orderId: Int, orderItem: OrderItem) {
        _state.value = CookCurrentOrderDetailVMStates.UpdatingData
        changeOrderItemStateUseCase.changeOrderItemStatus(orderId, orderItem, object : SimpleTask {
            override fun onSuccess(result: Unit) {
                _state.value = CookCurrentOrderDetailVMStates.OnUpdatingSuccess
            }

            override fun onError(message: ErrorMessage?) {
                _state.value = CookCurrentOrderDetailVMStates.OnUpdatingFailure(
                    message ?: defaultErrorMessage
                )
            }
        })
    }
}