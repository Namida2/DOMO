package com.example.domo.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

typealias States = RegistrationViewModelStates
sealed class RegistrationViewModelStates {
    object Default : RegistrationViewModelStates()
    object Validating : RegistrationViewModelStates()
    class Invalid() : RegistrationViewModelStates()
    class Valid() : RegistrationViewModelStates()
}

class RegistrationViewModel : ViewModel() {
    private var _state = MutableLiveData<States>(RegistrationViewModelStates.Default)
    fun validation() {

    }

}