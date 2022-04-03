package com.example.featureSettings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.menu.Category
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.menu.isTheSameMenu
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.Event
import com.example.core.domain.entities.tools.NetworkConnectionListener.networkConnectionChanges
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.interfaces.TerminatingState
import com.example.featureMenuDialog.domain.interfaces.OnDismissListener
import com.example.featureSettings.domain.useCases.SaveMenuUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class SettingsVMStates {
    object Default : SettingsVMStates()
    object SavingMenu : SettingsVMStates()
    object OnSavingSuccess : SettingsVMStates(), TerminatingState
    class OnSavingFailed(val message: ErrorMessage) : SettingsVMStates(), TerminatingState
}

class SettingsViewModel(
    private val saveMenuUseCase: SaveMenuUseCase
) : ViewModel(), OnDismissListener, Stateful<SettingsVMStates> {

    private lateinit var lastSavedMenu: MutableList<Category>
    private val _onMenuDialogDismissEvent = MutableLiveData<Event<Unit>>()
    val onMenuDialogDismissEvent: LiveData<Event<Unit>> = _onMenuDialogDismissEvent
    private val _state = MutableLiveData<SettingsVMStates>()
    val state: LiveData<SettingsVMStates> = _state

    init {
        viewModelScope.launch {
            networkConnectionChanges.collect { isConnected ->
                if(isConnected) return@collect
                if(state.value == SettingsVMStates.SavingMenu)
                    MenuService.setNewMenu(lastSavedMenu)
                setNewState(SettingsVMStates.OnSavingFailed(checkNetworkConnectionMessage))
            }
        }
    }

    override fun onDismiss() {
        if (MenuService.menu isTheSameMenu lastSavedMenu) return
        _onMenuDialogDismissEvent.value = Event(Unit)
    }

    fun onAcceptNewMenu() {
        _state.value = SettingsVMStates.SavingMenu
        saveMenuUseCase.saveNewMenu(object : SimpleTask {
            override fun onSuccess(result: Unit) {
                setNewState(SettingsVMStates.OnSavingSuccess)
            }
            override fun onError(message: ErrorMessage?) {
                MenuService.setNewMenu(lastSavedMenu)
                setNewState(SettingsVMStates.OnSavingFailed(message ?: defaultErrorMessage))
            }
        })
    }

    fun onCancelNewMenu() {
        MenuService.menu = lastSavedMenu
        MenuService.setNewMenu(lastSavedMenu)
    }

    fun sameMenuBeforeChanges() {
        lastSavedMenu = MenuService.copyMenu()
    }

    override fun setNewState(state: SettingsVMStates) {
        _state.value = state
        if(state is TerminatingState)
            _state.value = SettingsVMStates.Default
    }

}