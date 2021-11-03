package com.example.domo.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.R
import com.example.domo.models.LogInModel
import entities.Employee
import entities.ErrorMessage
import entities.TaskWithEmployee
import extentions.isEmptyField

sealed class LogInViewModelStates {
    open var errorMessage: ErrorMessage? = null

    object EmptyField : LogInViewModelStates() {
        override var errorMessage: ErrorMessage? = ErrorMessage(
            R.string.emptyFieldTitle,
            R.string.emptyFieldMessage
        )
    }

    object Validating : LogInViewModelStates()
    object WrongEmailOrPassword : LogInViewModelStates() {
        override var errorMessage: ErrorMessage? = ErrorMessage(
            R.string.wrongEmailOrPasswordTitle,
            R.string.wrongEmailOrPasswordMessage
        )
    }

    class Success(employee: Employee) : LogInViewModelStates()
    object Default : LogInViewModelStates()
}

class LogInViewModel(
    private val model: LogInModel
) : ViewModel() {
    private var _state = MutableLiveData<LogInViewModelStates>(LogInViewModelStates.Default)
    val state = _state

    fun signIn(email: String, password: String) {
        state.value = LogInViewModelStates.Validating
        if (isEmptyField(email, password)) {
            state.value = LogInViewModelStates.EmptyField; return
        }
        model.signIn(email, password, object : TaskWithEmployee {
            override fun onSuccess(arg: Employee?) {
                state.value = LogInViewModelStates.Success(arg!!)
            }
            override fun onError(arg: ErrorMessage) {
                state.value = LogInViewModelStates.WrongEmailOrPassword.apply { errorMessage = arg }
            }
        })
    }

    fun resetState() {
        state.value = LogInViewModelStates.Default
    }

}