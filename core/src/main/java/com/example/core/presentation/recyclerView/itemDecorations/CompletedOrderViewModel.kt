package com.example.core.presentation.recyclerView.itemDecorations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.interfaces.TerminatingState

sealed class CompletedOrderVMStates {
    object Default : CompletedOrderVMStates()
    object ShowDetail : CompletedOrderVMStates(), TerminatingState
    object DeletingOrder : CompletedOrderVMStates()
    object OrderDeleted : CompletedOrderVMStates(), TerminatingState
}

class CompletedOrderViewModel: ViewModel(), Stateful<CompletedOrderVMStates> {

    private val _state = MutableLiveData<CompletedOrderVMStates>()
    val state: LiveData<CompletedOrderVMStates> = _state

    fun onShowDetailButtonClick() {
        setNewState(CompletedOrderVMStates.ShowDetail)
    }

    fun onDeleteOrderButtonClick() {
        setNewState(CompletedOrderVMStates.DeletingOrder)
    }

    override fun setNewState(state: CompletedOrderVMStates) {
        _state.value = state
        if (state is TerminatingState)
            _state.value = CompletedOrderVMStates.Default
    }

}