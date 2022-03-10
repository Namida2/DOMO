package com.example.cookCore.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookCore.domain.useCases.ChangeOrderItemStateUseCase
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.ErrorMessages.defaultErrorMessage
import com.example.core.domain.tools.SimpleTask

sealed class CookCurrentOrderDetailVMStates {
    object UpdatingData : CookCurrentOrderDetailVMStates()
    object OnUpdatingSuccess : CookCurrentOrderDetailVMStates()
    class OnUpdationgFailure(val errorMessage: ErrorMessage) : CookCurrentOrderDetailVMStates()
    object Default : CookCurrentOrderDetailVMStates()
}

class CookCurrentOrderDetailViewModel(
    private val changeOrderItemStateUseCase: ChangeOrderItemStateUseCase
) : ViewModel() {

    private val _state = MutableLiveData<CookCurrentOrderDetailVMStates>()
    val state: LiveData<CookCurrentOrderDetailVMStates> = _state

    fun setOrderItemAsReady(orderId: Int, orderItemId: String) {
        _state.value = CookCurrentOrderDetailVMStates.UpdatingData
        changeOrderItemStateUseCase.setOrderItemAsReady(orderId, orderItemId, object : SimpleTask {
            override fun onSuccess(arg: Unit) {
                _state.value = CookCurrentOrderDetailVMStates.OnUpdatingSuccess
            }

            override fun onError(message: ErrorMessage?) {
                _state.value = CookCurrentOrderDetailVMStates.OnUpdationgFailure(
                    message ?: defaultErrorMessage
                )
            }
        })
    }
}