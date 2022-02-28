package com.example.domo.viewModels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.adapters.TablesAdapter
import com.example.core.domain.tools.Event

typealias OnTableClickEvent = com.example.core.domain.tools.Event<TablesAdapter.ViewOwner>

sealed class TablesVMStates {
    object ShowingOrderFragment: TablesVMStates()
    object Default : TablesVMStates()
}

class TablesViewModel : ViewModel() {
    private var _state = MutableLiveData<TablesVMStates>(TablesVMStates.Default)
    val state: LiveData<TablesVMStates> = _state

    private var _onTableSelected = MutableLiveData<OnTableClickEvent>()
    var onTableSelected: LiveData<OnTableClickEvent> = _onTableSelected

    fun onTableClick(viewOwner: TablesAdapter.ViewOwner) {
        if(state.value == TablesVMStates.ShowingOrderFragment) return
        _onTableSelected.value = com.example.core.domain.tools.Event(viewOwner)
        _state.value = TablesVMStates.ShowingOrderFragment
    }
    fun resetState() {
        if(state.value == TablesVMStates.Default) return
        _state.value = TablesVMStates.Default
    }
}