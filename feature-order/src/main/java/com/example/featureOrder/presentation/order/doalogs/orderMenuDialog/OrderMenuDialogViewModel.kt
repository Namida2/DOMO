package com.example.featureOrder.presentation.order.doalogs.orderMenuDialog

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.interfaces.TerminatingState
import com.example.featureOrder.domain.useCases.InsertOrderUseCase

sealed class OrderMenuDialogVMStates {
    object Default : OrderMenuDialogVMStates()
    object InsertingCurrentOrder : OrderMenuDialogVMStates()
    object InsertingWasSuccessful : OrderMenuDialogVMStates(), TerminatingState
    class InsertingWasFailure(val errorMessage: ErrorMessage) : OrderMenuDialogVMStates(),
        TerminatingState
}

class OrderMenuDialogViewModel(
    private val insertOrderUseCase: InsertOrderUseCase,
) : ViewModel(), Stateful<OrderMenuDialogVMStates> {

    private var _state: MutableLiveData<OrderMenuDialogVMStates> = MutableLiveData()
    val state: LiveData<OrderMenuDialogVMStates> = _state

    fun onConfirmOrderButtonClick() {
        if(state.value == OrderMenuDialogVMStates.InsertingCurrentOrder) return
        setNewState(OrderMenuDialogVMStates.InsertingCurrentOrder)
        insertOrderUseCase.insertCurrentOrder(object : SimpleTask {
            override fun onSuccess(result: Unit) {
                setNewState(OrderMenuDialogVMStates.InsertingWasSuccessful)
            }
            override fun onError(message: ErrorMessage?) {
                setNewState(
                    OrderMenuDialogVMStates.InsertingWasFailure(
                        message ?: defaultErrorMessage
                    )
                )
            }
        })
    }

    override fun setNewState(state: OrderMenuDialogVMStates) {
        _state.value = state
        if (state is TerminatingState)
            _state.value = OrderMenuDialogVMStates.Default
    }


}