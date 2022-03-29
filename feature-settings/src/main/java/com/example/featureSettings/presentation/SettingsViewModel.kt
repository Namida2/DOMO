package com.example.featureSettings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.menu.Category
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.menu.isTheSameMenu
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.Event
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.ErrorMessages.defaultErrorMessage
import com.example.featureMenuDialog.domain.interfaces.OnDismissListener
import com.example.featureSettings.domain.useCases.SaveMenuUseCase

sealed class SettingsVMStates {
    object Default : SettingsVMStates()
    object SavingMenu : SettingsVMStates()
    object OnSavingSuccess : SettingsVMStates()
    class OnSavingFailed(val message: ErrorMessage) : SettingsVMStates()
}

class SettingsViewModel(
    private val saveMenuUseCase: SaveMenuUseCase
) : ViewModel(), OnDismissListener {

    private lateinit var lastSavedMenu: MutableList<Category>
    private val _onMenuDialogDismissEvent = MutableLiveData<Event<Unit>>()
    val onMenuDialogDismissEvent: LiveData<Event<Unit>> = _onMenuDialogDismissEvent
    private val _state = MutableLiveData<SettingsVMStates>()
    val state: LiveData<SettingsVMStates> = _state

    override fun onDismiss() {
        if (MenuService.menu isTheSameMenu lastSavedMenu) return
        _onMenuDialogDismissEvent.value = Event(Unit)
    }

    fun onAcceptNewMenu() {
        _state.value = SettingsVMStates.SavingMenu
        saveMenuUseCase.saveNewMenu(object : SimpleTask {
            override fun onSuccess(result: Unit) {
                _state.value = SettingsVMStates.OnSavingSuccess
            }
            override fun onError(message: ErrorMessage?) {
                _state.value = SettingsVMStates.OnSavingFailed(message ?: defaultErrorMessage)
            }
        })
    }

    fun onCancelNewMenu() {
        MenuService.menu = lastSavedMenu
    }

    fun sameMenuBeforeChanges() {
        lastSavedMenu = MenuService.copyMenu()
    }

}