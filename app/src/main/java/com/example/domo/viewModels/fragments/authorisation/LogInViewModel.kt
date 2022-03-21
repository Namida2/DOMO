package com.example.domo.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.Employee
import com.example.domo.R
import com.example.domo.models.interfaces.LogInModelInterface

sealed class LogInViewModelStates {
    open var errorMessage: com.example.core.domain.tools.ErrorMessage? = null

    object EmptyField : LogInViewModelStates() {
        override var errorMessage: com.example.core.domain.tools.ErrorMessage? =
            com.example.core.domain.tools.ErrorMessage(
                R.string.emptyFieldTitle,
                R.string.emptyFieldMessage
            )
    }

    object Validating : LogInViewModelStates()
    object WrongEmailOrPassword : LogInViewModelStates() {
        override var errorMessage: com.example.core.domain.tools.ErrorMessage? =
            com.example.core.domain.tools.ErrorMessage(
                R.string.wrongEmailOrPasswordTitle,
                R.string.wrongEmailOrPasswordMessage
            )
    }

    class Success(employee: Employee) : LogInViewModelStates()
    object Default : LogInViewModelStates()
}

class LogInViewModel(
    private val model: LogInModelInterface
) : ViewModel() {
    private var _state = MutableLiveData<LogInViewModelStates>(LogInViewModelStates.Default)
    val state = _state

    fun signIn(email: String, password: String) {
        state.value = LogInViewModelStates.Validating
        if (com.example.core.domain.tools.extensions.isEmptyField(email, password)) {
            state.value = LogInViewModelStates.EmptyField; return
        }
        model.signIn(email, password, object : com.example.core.domain.tools.TaskWithEmployee {
            override fun onSuccess(result: Employee) {
                state.value = LogInViewModelStates.Success(result)
            }

            override fun onError(message: com.example.core.domain.tools.ErrorMessage?) {
                state.value =
                    LogInViewModelStates.WrongEmailOrPassword.apply { errorMessage = message }
            }
        })
    }

    fun resetState() {
        state.value = LogInViewModelStates.Default
    }

}