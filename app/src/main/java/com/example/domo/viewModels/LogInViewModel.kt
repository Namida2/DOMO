package com.example.domo.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.R
import com.example.domo.models.LogInModel
import entities.ErrorMessage
import entities.Task
import entities.TaskWithErrorMessage
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
    object EmailDoesNotExists : LogInViewModelStates() {
        override var errorMessage: ErrorMessage? = ErrorMessage(
            R.string.wrongEmailOrPasswordTitle,
            R.string.wrongEmailOrPasswordMessage
        )
    }

    object Success : LogInViewModelStates()
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
        //TODO: "SignIn"
        model.signIn(email, password, object : TaskWithErrorMessage {
            override fun onSuccess(arg: Unit) {

            }

            override fun onError(arg: ErrorMessage) {

            }

        })
    }

    fun resetState() {
        state.value = LogInViewModelStates.Default
    }

}