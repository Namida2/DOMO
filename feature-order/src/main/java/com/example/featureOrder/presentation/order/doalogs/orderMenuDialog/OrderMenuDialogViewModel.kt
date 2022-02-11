package com.example.featureOrder.presentation.order.doalogs.orderMenuDialog

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.featureOrder.domain.useCases.InsertOrderUseCase
import com.example.waiterCore.domain.tools.ErrorMessage

import com.example.waiterCore.domain.tools.ErrorMessages.defaultErrorMessage
import com.example.waiterCore.domain.tools.SimpleTask

sealed class OrderMenuDialogVMStates {
    object Default: OrderMenuDialogVMStates()
    object InsertingCurrentOrder: OrderMenuDialogVMStates()
    object InsertingWasSuccessful: OrderMenuDialogVMStates()
    class InsertingWasFailure: OrderMenuDialogVMStates() {
        val errorMasse = defaultErrorMessage
    }
}
class OrderMenuDialogViewModel(
    private val insertOrderUseCase: InsertOrderUseCase,
//    private val model: OrderMenuDialogModelInterface,
) : ViewModel() {

    private var _state: MutableLiveData<OrderMenuDialogVMStates> = MutableLiveData(
    OrderMenuDialogVMStates.Default)
    val state: LiveData<OrderMenuDialogVMStates> = _state

    fun onConfirmOrderButtonClick(view: View) {
        _state.value = OrderMenuDialogVMStates.InsertingCurrentOrder
        insertOrderUseCase.insertCurrentOrder(object: SimpleTask {
            override fun onSuccess(arg: Unit) {
                _state.value = OrderMenuDialogVMStates.InsertingWasSuccessful
            }
            override fun onError(message: ErrorMessage?) {
                _state.value = OrderMenuDialogVMStates.InsertingWasFailure()
            }
        })
    }
}