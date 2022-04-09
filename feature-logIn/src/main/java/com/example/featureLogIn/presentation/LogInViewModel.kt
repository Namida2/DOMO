package com.example.featureLogIn.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.Employee
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.TaskWithEmployee
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.entities.tools.constants.Messages.emptyFieldMessage
import com.example.core.domain.entities.tools.extensions.isEmptyField
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.interfaces.TerminatingState
import com.example.featureLogIn.domain.LogInUseCase
import kotlinx.coroutines.flow.StateFlow

sealed class LogInVMStates {
    object Validating : LogInVMStates()
    class OnFailure(val errorMessage: ErrorMessage): LogInVMStates(), TerminatingState
    class Success(val employee: Employee) : LogInVMStates(), TerminatingState
    object Default : LogInVMStates()
}

class LogInViewModel(
    private val logInUseCaseImpl: LogInUseCase,
) : ViewModel(), Stateful<LogInVMStates>{

    private var _state = MutableLiveData<LogInVMStates>()
    val state: LiveData<LogInVMStates> = _state

    fun logIn(email: String, password: String) {
        setNewState(LogInVMStates.Validating)
        if (isEmptyField(email, password)) {
            setNewState(LogInVMStates.OnFailure(emptyFieldMessage))
            return
        }
        logInUseCaseImpl.logIn(email, password, object : TaskWithEmployee {
            override fun onSuccess(result: Employee) {
                setNewState(LogInVMStates.Success(result))
            }
            override fun onError(message: ErrorMessage?) {
                setNewState(LogInVMStates.OnFailure(message ?: defaultErrorMessage))
            }
        })
    }

    override fun setNewState(state: LogInVMStates) {
        _state.value = state
        if(state is TerminatingState)
            _state.value = LogInVMStates.Default
    }

}