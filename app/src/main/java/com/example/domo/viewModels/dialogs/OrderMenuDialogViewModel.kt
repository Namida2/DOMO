package com.example.domo.viewModels.dialogs

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.models.interfaces.OrderMenuDialogModelInterface
import entities.tools.ErrorMessage
import entities.tools.SimpleTask
import tools.dialogs.ErrorMessages.somethingWrongMessage

sealed class OrderMenuDialogVMStates {
    object Default: OrderMenuDialogVMStates()
    object InsertingCurrentOrder: OrderMenuDialogVMStates()
    object InsertingWasSuccessful: OrderMenuDialogVMStates()
    class InsertingWasFailure: OrderMenuDialogVMStates() {
        val errorMasse = somethingWrongMessage
    }
}
class OrderMenuDialogViewModel(
    private val model: OrderMenuDialogModelInterface,
) : ViewModel() {

    private var _state: MutableLiveData<OrderMenuDialogVMStates> = MutableLiveData(OrderMenuDialogVMStates.Default)
    val state: LiveData<OrderMenuDialogVMStates> = _state

    fun onConfirmOrderButtonClick(view: View) {
        _state.value = OrderMenuDialogVMStates.InsertingCurrentOrder
        model.insertCurrentOrder(object: SimpleTask {
            override fun onSuccess(arg: Unit) {
                _state.value = OrderMenuDialogVMStates.InsertingWasSuccessful
            }

            override fun onError(message: ErrorMessage?) {
                _state.value = OrderMenuDialogVMStates.InsertingWasFailure()
            }

        })
    }
}