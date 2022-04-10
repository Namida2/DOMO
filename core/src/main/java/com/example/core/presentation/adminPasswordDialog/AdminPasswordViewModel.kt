package com.example.core.presentation.adminPasswordDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.tools.Event

class AdminPasswordViewModel(
    private val correctPassword: String
): ViewModel() {
    private val _onPasswordCheckedEvent = MutableLiveData<Event<Boolean>>()
    val onPasswordCheckedEvent:LiveData<Event<Boolean>> = _onPasswordCheckedEvent
    fun onConfirmPasswordButtonClock(password: String) {
        _onPasswordCheckedEvent.value = Event(correctPassword == password)
    }
}