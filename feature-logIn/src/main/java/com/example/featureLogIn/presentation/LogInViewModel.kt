package com.example.featureLogIn.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.TaskWithEmployee
import com.example.core.domain.entities.tools.constants.ErrorMessages.emptyFieldMessage
import com.example.core.domain.entities.tools.constants.ErrorMessages.wrongEmailOrPassword
import com.example.core.domain.entities.tools.extensions.isEmptyField
import com.example.featureLogIn.domain.LogInUseCase

sealed class LogInViewModelStates {
    open var errorMessage: ErrorMessage? = null

    object EmptyField : LogInViewModelStates() {
        override var errorMessage: ErrorMessage? = emptyFieldMessage
    }

    object Validating : LogInViewModelStates()
    object WrongEmailOrPassword : LogInViewModelStates() {
        override var errorMessage: ErrorMessage? =
            wrongEmailOrPassword
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
            override fun onSuccess(result: Employee) {
                _state.value = LogInViewModelStates.Success(result)
            }
            override fun onError(message: ErrorMessage?) {
                _state.value = LogInViewModelStates.WrongEmailOrPassword.apply {
                        errorMessage = message
                    }
            }
        })
    }

    fun resetState() {
        _state.value = LogInViewModelStates.Default
    }

}