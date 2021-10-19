package com.example.domo.viewModels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.views.log
import entities.Employee

typealias States = RegistrationViewModelStates
sealed class RegistrationViewModelStates {
    object Default : RegistrationViewModelStates()
    object Validating : RegistrationViewModelStates()
    object Invalid : RegistrationViewModelStates()
    class Valid(employee: Employee) : RegistrationViewModelStates()
}

class RegistrationViewModel : ViewModel() {
    private var _state = MutableLiveData<States>(RegistrationViewModelStates.Default)
    val state = _state

    fun validation(name: String, email: String, password: String, confirmPassword: String) {
        return name.isEmpty()
    }

}