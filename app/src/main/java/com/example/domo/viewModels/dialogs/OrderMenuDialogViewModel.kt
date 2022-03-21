package com.example.domo.viewModels.dialogs

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.models.interfaces.OrderMenuDialogModelInterface
import com.example.core.domain.tools.constants.ErrorMessages.defaultErrorMessage

sealed class OrderMenuDialogVMStates {
    object Default: OrderMenuDialogVMStates()
    object InsertingCurrentOrder: OrderMenuDialogVMStates()
    object InsertingWasSuccessful: OrderMenuDialogVMStates()
    class InsertingWasFailure: OrderMenuDialogVMStates() {
        val errorMasse = defaultErrorMessage
    }
}
class OrderMenuDialogViewModel(
    private val model: OrderMenuDialogModelInterface,
) : ViewModel() {

    private var _state: MutableLiveData<OrderMenuDialogVMStates> = MutableLiveData(OrderMenuDialogVMStates.Default)
    val state: LiveData<OrderMenuDialogVMStates> = _state

    fun onConfirmOrderButtonClick(view: View) {
        _state.value = OrderMenuDialogVMStates.InsertingCurrentOrder
        model.insertCurrentOrder(object: com.example.core.domain.tools.SimpleTask {
            override fun onSuccess(result: Unit) {
                _state.value = OrderMenuDialogVMStates.InsertingWasSuccessful
            }

            override fun onError(message: com.example.core.domain.tools.ErrorMessage?) {
                _state.value = OrderMenuDialogVMStates.InsertingWasFailure()
            }

        })
    }
}