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
    object EmptyField: RegistrationViewModelStates()
    object ShortPassword: RegistrationViewModelStates()
    object WrongPasswordConfirmation: RegistrationViewModelStates()
    class Valid(employee: Employee) : RegistrationViewModelStates()
}

class RegistrationViewModel : ViewModel() {
    private val MIN_PASSWORD_LENGH = 6
    private var _state = MutableLiveData<States>(RegistrationViewModelStates.Default)
    val state = _state

    fun validation(name: String, email: String, password: String, confirmPassword: String) {
        if(anyFieldIsEmpty(name, email, password, confirmPassword)) {
            state.value = RegistrationViewModelStates.EmptyField
            return
        }
        if(password.length < MIN_PASSWORD_LENGH) {
            state.value = RegistrationViewModelStates.ShortPassword
            return
        }
        if(password != confirmPassword) {
            state.value = RegistrationViewModelStates.WrongPasswordConfirmation
            return
        }
    }

    private fun anyFieldIsEmpty(name: String, email: String, password: String, confirmPassword: String): Boolean {
        return  name.isEmpty() || name.replace(" ", "").isEmpty() ||
                email.isEmpty() || email.replace(" ", "").isEmpty() ||
                password.isEmpty() || password.replace(" ", "").isEmpty() ||
                confirmPassword.isEmpty() || confirmPassword.replace(" ", "").isEmpty()
    }


}