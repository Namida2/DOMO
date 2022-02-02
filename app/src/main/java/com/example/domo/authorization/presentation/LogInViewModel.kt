package com.example.domo.authorization.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.authorization.domain.LogInUseCase
import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.tools.ErrorMessage
import com.example.waiterCore.domain.tools.ErrorMessages.emptyFieldMessage
import com.example.waiterCore.domain.tools.ErrorMessages.wrongEmailOrPassword
import com.example.waiterCore.domain.tools.TaskWithEmployee
import com.example.waiterCore.domain.tools.extensions.isEmptyField

sealed class LogInViewModelStates {
    open var errorMessage: ErrorMessage? = null

    object EmptyField : LogInViewModelStates() {
        override var errorMessage: ErrorMessage? = emptyFieldMessage
    }
    object Validating : LogInViewModelStates()
    object WrongEmailOrPassword : LogInViewModelStates() {
        override var errorMessage: ErrorMessage? = wrongEmailOrPassword
    }
    class Success(val employee: Employee) : LogInViewModelStates()
    object Default : LogInViewModelStates()
}

class LogInViewModel(
    private val logInUseCaseImpl: LogInUseCase,
) : ViewModel() {
    private var _state = MutableLiveData<LogInViewModelStates>(LogInViewModelStates.Default)
    val state: LiveData<LogInViewModelStates> = _state

    fun logIn(email: String, password: String) {
        _state.value = LogInViewModelStates.Validating
        if (isEmptyField(email, password)) {
            _state.value = LogInViewModelStates.EmptyField; return
        }
        logInUseCaseImpl.logIn(email, password, object : TaskWithEmployee {
            override fun onSuccess(arg: Employee) {
                _state.value = LogInViewModelStates.Success(arg)
            }

            override fun onError(message: ErrorMessage?) {
                _state.value =
                    LogInViewModelStates.WrongEmailOrPassword.apply {
                        errorMessage = message
                    }
            }
        })
    }

    fun resetState() {
        _state.value = LogInViewModelStates.Default
    }

}