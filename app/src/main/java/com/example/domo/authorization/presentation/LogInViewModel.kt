package com.example.domo.authorization.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.R
import com.example.domo.authorization.domain.LogInUseCaseImpl
import com.example.waiter_core.domain.tools.ErrorMessage
import com.example.waiter_core.domain.tools.TaskWithEmployee
import com.example.waiter_core.domain.Employee
import com.example.waiter_core.domain.tools.ErrorMessages.emptyFieldMessage
import com.example.waiter_core.domain.tools.ErrorMessages.wrongEmailOrPassword
import com.example.waiter_core.domain.tools.extensions.isEmptyField

sealed class LogInViewModelStates {
    open var errorMessage: ErrorMessage? = null

    object EmptyField : LogInViewModelStates() {
        override var errorMessage: ErrorMessage? = emptyFieldMessage
    }
    object Validating : LogInViewModelStates()
    object WrongEmailOrPassword : LogInViewModelStates() {
        override var errorMessage: ErrorMessage? = wrongEmailOrPassword
    }
    class Success(employee: Employee) : LogInViewModelStates()
    object Default : LogInViewModelStates()
}

class LogInViewModel(
    private val logInUseCaseImpl: LogInUseCaseImpl
) : ViewModel() {
    private var _state = MutableLiveData<LogInViewModelStates>(LogInViewModelStates.Default)
    val state = _state

    fun logIn(email: String, password: String) {
        state.value = LogInViewModelStates.Validating
        if (isEmptyField(email, password)) {
            state.value = LogInViewModelStates.EmptyField; return
        }
        logInUseCaseImpl.logIn(email, password, object : TaskWithEmployee {
            override fun onSuccess(arg: Employee) {
                state.value = LogInViewModelStates.Success(arg)
            }
            override fun onError(message: ErrorMessage?) {
                state.value = LogInViewModelStates.WrongEmailOrPassword.apply { errorMessage = message }
            }
        })
    }

    fun resetState() {
        state.value = LogInViewModelStates.Default
    }

}