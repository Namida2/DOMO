package com.example.featureOrder.presentation.order.doalogs.orderMenuDialog

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.constants.ErrorMessages.defaultErrorMessage
import com.example.core.domain.entities.tools.SimpleTask
import com.example.featureOrder.domain.useCases.InsertOrderUseCase

sealed class OrderMenuDialogVMStates {
    object Default : OrderMenuDialogVMStates()
    object InsertingCurrentOrder : OrderMenuDialogVMStates()
    object InsertingWasSuccessful : OrderMenuDialogVMStates()
    class InsertingWasFailure(val errorMessage: ErrorMessage) : OrderMenuDialogVMStates()
}

class OrderMenuDialogViewModel(
    private val insertOrderUseCase: InsertOrderUseCase,
) : ViewModel() {

    private var _state: MutableLiveData<OrderMenuDialogVMStates> = MutableLiveData(
        OrderMenuDialogVMStates.Default
    )
    val state: LiveData<OrderMenuDialogVMStates> = _state

    fun onConfirmOrderButtonClick(view: View) {
        _state.value = OrderMenuDialogVMStates.InsertingCurrentOrder
        insertOrderUseCase.insertCurrentOrder(object : SimpleTask {
            override fun onSuccess(result: Unit) {
                _state.value = OrderMenuDialogVMStates.InsertingWasSuccessful
            }

            override fun onError(message: ErrorMessage?) {
                _state.value =
                    OrderMenuDialogVMStates.InsertingWasFailure(message ?: defaultErrorMessage)
            }
        })
    }
}