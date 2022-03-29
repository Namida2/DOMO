package com.example.featureCurrentOrders.presentation.completedOrderMenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.ErrorMessages.defaultErrorMessage
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.interfaces.TerminatingState
import com.example.featureCurrentOrders.domain.useCases.DeleteOrderUseCase
import kotlin.properties.Delegates

sealed class CompletedOrderVMStates {
    object Default : CompletedOrderVMStates()
    object ShowDetail : CompletedOrderVMStates(), TerminatingState
    object DeletingOrder : CompletedOrderVMStates()
    object OrderDeleted : CompletedOrderVMStates(), TerminatingState
    class OnOrderDeletingFailure(
        val errorMessage: ErrorMessage
    ) : CompletedOrderVMStates(), TerminatingState
}

class CompletedOrderViewModel(
    private val deleteOrderUseCase: DeleteOrderUseCase
) : ViewModel(), Stateful<CompletedOrderVMStates>, SimpleTask {

    var orderId by Delegates.notNull<Int>()

    private val _state = MutableLiveData<CompletedOrderVMStates>()
    val state: LiveData<CompletedOrderVMStates> = _state

    fun onShowDetailButtonClick() {
        setNewState(CompletedOrderVMStates.ShowDetail)
    }

    fun onDeleteOrderButtonClick() {
        setNewState(CompletedOrderVMStates.DeletingOrder)
        deleteOrderUseCase.deleteOrder(orderId, this)
    }

    override fun onSuccess(result: Unit) {
        setNewState(CompletedOrderVMStates.OrderDeleted)
    }

    override fun onError(message: ErrorMessage?) {
        setNewState(CompletedOrderVMStates.OnOrderDeletingFailure(message ?: defaultErrorMessage))
    }

    override fun setNewState(state: CompletedOrderVMStates) {
        _state.value = state
        if (state is TerminatingState)
            _state.value = CompletedOrderVMStates.Default
    }

}